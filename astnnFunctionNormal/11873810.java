class BackupThread extends Thread {
    public void decode(final File srcPath, final File destPath) throws IOException {
        byte[] header = new byte[2048];
        byte[] payload = new byte[65536];
        byte[] decdat = new byte[44100 * 2 * 2];
        final int WAV_HEADERSIZE = 8;
        final short WAVE_FORMAT_SPEEX = (short) 0xa109;
        final String RIFF = "RIFF";
        final String WAVE = "WAVE";
        final String FORMAT = "fmt ";
        final String DATA = "data";
        final int OGG_HEADERSIZE = 27;
        final int OGG_SEGOFFSET = 26;
        final String OGGID = "OggS";
        int segments = 0;
        int curseg = 0;
        int bodybytes = 0;
        int decsize = 0;
        int packetNo = 0;
        if (printlevel <= INFO) version();
        if (printlevel <= DEBUG) log("");
        if (printlevel <= DEBUG) log("Input File: " + srcPath);
        speexDecoder = new SpeexDecoder();
        DataInputStream dis = new DataInputStream(new FileInputStream(srcPath));
        AudioFileWriter writer = null;
        int origchksum;
        int chksum;
        try {
            while (true) {
                if (srcFormat == FILE_FORMAT_OGG) {
                    dis.readFully(header, 0, OGG_HEADERSIZE);
                    origchksum = readInt(header, 22);
                    header[22] = 0;
                    header[23] = 0;
                    header[24] = 0;
                    header[25] = 0;
                    chksum = OggCrc.checksum(0, header, 0, OGG_HEADERSIZE);
                    if (!OGGID.equals(new String(header, 0, 4))) {
                        log("missing ogg id!");
                        return;
                    }
                    segments = header[OGG_SEGOFFSET] & 0xFF;
                    dis.readFully(header, OGG_HEADERSIZE, segments);
                    chksum = OggCrc.checksum(chksum, header, OGG_HEADERSIZE, segments);
                    for (curseg = 0; curseg < segments; curseg++) {
                        bodybytes = header[OGG_HEADERSIZE + curseg] & 0xFF;
                        if (bodybytes == 255) {
                            log("sorry, don't handle 255 sizes!");
                            return;
                        }
                        dis.readFully(payload, 0, bodybytes);
                        chksum = OggCrc.checksum(chksum, payload, 0, bodybytes);
                        if (packetNo == 0) {
                            if (readSpeexHeader(payload, 0, bodybytes)) {
                                if (printlevel <= DEBUG) {
                                    log("File Format: Ogg Speex");
                                    log("Sample Rate: " + sampleRate);
                                    log("Channels: " + channels);
                                    log("Encoder mode: " + (mode == 0 ? "Narrowband" : (mode == 1 ? "Wideband" : "UltraWideband")));
                                    log("Frames per packet: " + nframes);
                                }
                                if (destFormat == FILE_FORMAT_WAVE) {
                                    writer = new PcmWaveWriter(speexDecoder.getSampleRate(), speexDecoder.getChannels());
                                    if (printlevel <= DEBUG) {
                                        log("");
                                        log("Output File: " + destPath);
                                        log("File Format: PCM Wave");
                                        log("Perceptual Enhancement: " + enhanced);
                                    }
                                } else {
                                    writer = new RawWriter();
                                    if (printlevel <= DEBUG) {
                                        log("");
                                        log("Output File: " + destPath);
                                        log("File Format: Raw Audio");
                                        log("Perceptual Enhancement: " + enhanced);
                                    }
                                }
                                writer.open(destPath);
                                writer.writeHeader(null);
                                packetNo++;
                            } else {
                                packetNo = 0;
                            }
                        } else if (packetNo == 1) {
                            packetNo++;
                        } else {
                            if (loss > 0 && random.nextInt(100) < loss) {
                                speexDecoder.processData(null, 0, bodybytes);
                                for (int i = 1; i < nframes; i++) {
                                    speexDecoder.processData(true);
                                }
                            } else {
                                speexDecoder.processData(payload, 0, bodybytes);
                                for (int i = 1; i < nframes; i++) {
                                    speexDecoder.processData(false);
                                }
                            }
                            if ((decsize = speexDecoder.getProcessedData(decdat, 0)) > 0) {
                                writer.writePacket(decdat, 0, decsize);
                            }
                            packetNo++;
                        }
                    }
                    if (chksum != origchksum) throw new IOException("Ogg CheckSums do not match");
                } else {
                    if (packetNo == 0) {
                        if (srcFormat == FILE_FORMAT_WAVE) {
                            dis.readFully(header, 0, WAV_HEADERSIZE + 4);
                            if (!RIFF.equals(new String(header, 0, 4)) && !WAVE.equals(new String(header, 8, 4))) {
                                log("Not a WAVE file");
                                return;
                            }
                            dis.readFully(header, 0, WAV_HEADERSIZE);
                            String chunk = new String(header, 0, 4);
                            int size = readInt(header, 4);
                            while (!chunk.equals(DATA)) {
                                dis.readFully(header, 0, size);
                                if (chunk.equals(FORMAT)) {
                                    if (readShort(header, 0) != WAVE_FORMAT_SPEEX) {
                                        log("Not a Wave Speex file");
                                        return;
                                    }
                                    channels = readShort(header, 2);
                                    sampleRate = readInt(header, 4);
                                    bodybytes = readShort(header, 12);
                                    if (readShort(header, 16) < 82) {
                                        log("Possibly corrupt Speex Wave file.");
                                        return;
                                    }
                                    readSpeexHeader(header, 20, 80);
                                    if (printlevel <= DEBUG) {
                                        log("File Format: Wave Speex");
                                        log("Sample Rate: " + sampleRate);
                                        log("Channels: " + channels);
                                        log("Encoder mode: " + (mode == 0 ? "Narrowband" : (mode == 1 ? "Wideband" : "UltraWideband")));
                                        log("Frames per packet: " + nframes);
                                    }
                                }
                                dis.readFully(header, 0, WAV_HEADERSIZE);
                                chunk = new String(header, 0, 4);
                                size = readInt(header, 4);
                            }
                            if (printlevel <= DEBUG) log("Data size: " + size);
                        } else {
                            if (printlevel <= DEBUG) {
                                log("File Format: Raw Speex");
                                log("Sample Rate: " + sampleRate);
                                log("Channels: " + channels);
                                log("Encoder mode: " + (mode == 0 ? "Narrowband" : (mode == 1 ? "Wideband" : "UltraWideband")));
                                log("Frames per packet: " + nframes);
                            }
                            speexDecoder.init(mode, sampleRate, channels, enhanced);
                            if (!vbr) {
                                switch(mode) {
                                    case 0:
                                        bodybytes = NbEncoder.NB_FRAME_SIZE[NbEncoder.NB_QUALITY_MAP[quality]];
                                        break;
                                    case 1:
                                        bodybytes = SbEncoder.NB_FRAME_SIZE[SbEncoder.NB_QUALITY_MAP[quality]];
                                        bodybytes += SbEncoder.SB_FRAME_SIZE[SbEncoder.WB_QUALITY_MAP[quality]];
                                        break;
                                    case 2:
                                        bodybytes = SbEncoder.NB_FRAME_SIZE[SbEncoder.NB_QUALITY_MAP[quality]];
                                        bodybytes += SbEncoder.SB_FRAME_SIZE[SbEncoder.WB_QUALITY_MAP[quality]];
                                        bodybytes += SbEncoder.SB_FRAME_SIZE[SbEncoder.UWB_QUALITY_MAP[quality]];
                                        break;
                                    default:
                                }
                                bodybytes = (bodybytes + 7) >> 3;
                            } else {
                                bodybytes = 0;
                            }
                        }
                        if (destFormat == FILE_FORMAT_WAVE) {
                            writer = new PcmWaveWriter(sampleRate, channels);
                            if (printlevel <= DEBUG) {
                                log("");
                                log("Output File: " + destPath);
                                log("File Format: PCM Wave");
                                log("Perceptual Enhancement: " + enhanced);
                            }
                        } else {
                            writer = new RawWriter();
                            if (printlevel <= DEBUG) {
                                log("");
                                log("Output File: " + destPath);
                                log("File Format: Raw Audio");
                                log("Perceptual Enhancement: " + enhanced);
                            }
                        }
                        writer.open(destPath);
                        writer.writeHeader(null);
                        packetNo++;
                    } else {
                        dis.readFully(payload, 0, bodybytes);
                        if (loss > 0 && random.nextInt(100) < loss) {
                            speexDecoder.processData(null, 0, bodybytes);
                            for (int i = 1; i < nframes; i++) {
                                speexDecoder.processData(true);
                            }
                        } else {
                            speexDecoder.processData(payload, 0, bodybytes);
                            for (int i = 1; i < nframes; i++) {
                                speexDecoder.processData(false);
                            }
                        }
                        if ((decsize = speexDecoder.getProcessedData(decdat, 0)) > 0) {
                            writer.writePacket(decdat, 0, decsize);
                        }
                        packetNo++;
                    }
                }
            }
        } catch (EOFException eof) {
        }
        writer.close();
    }
}
