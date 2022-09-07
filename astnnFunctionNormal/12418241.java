class BackupThread extends Thread {
    public void parse(InputFile f, Format ext, int type, boolean thumbOnly) {
        int i = 0;
        while (isParsing()) {
            if (i == 5) {
                setMediaparsed(true);
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            i++;
        }
        if (isMediaparsed()) {
            return;
        }
        if (f != null) {
            if (f.getFile() != null) {
                setSize(f.getFile().length());
            } else {
                setSize(f.getSize());
            }
            ProcessWrapperImpl pw = null;
            boolean ffmpeg_parsing = true;
            if (type == Format.AUDIO || ext instanceof AudioAsVideo) {
                ffmpeg_parsing = false;
                DLNAMediaAudio audio = new DLNAMediaAudio();
                if (f.getFile() != null) {
                    try {
                        AudioFile af = AudioFileIO.read(f.getFile());
                        AudioHeader ah = af.getAudioHeader();
                        if (ah != null && !thumbOnly) {
                            int length = ah.getTrackLength();
                            int rate = ah.getSampleRateAsNumber();
                            if (ah.getEncodingType().toLowerCase().contains("flac 24")) {
                                audio.setBitsperSample(24);
                            }
                            audio.setSampleFrequency("" + rate);
                            setDuration((double) length);
                            setBitrate((int) ah.getBitRateAsNumber());
                            audio.setNrAudioChannels(2);
                            if (ah.getChannels() != null && ah.getChannels().toLowerCase().contains("mono")) {
                                audio.setNrAudioChannels(1);
                            } else if (ah.getChannels() != null && ah.getChannels().toLowerCase().contains("stereo")) {
                                audio.setNrAudioChannels(2);
                            } else if (ah.getChannels() != null) {
                                audio.setNrAudioChannels(Integer.parseInt(ah.getChannels()));
                            }
                            audio.setCodecA(ah.getEncodingType().toLowerCase());
                            if (audio.getCodecA().contains("(windows media")) {
                                audio.setCodecA(audio.getCodecA().substring(0, audio.getCodecA().indexOf("(windows media")).trim());
                            }
                        }
                        Tag t = af.getTag();
                        if (t != null) {
                            if (t.getArtworkList().size() > 0) {
                                setThumb(t.getArtworkList().get(0).getBinaryData());
                            } else {
                                if (PMS.getConfiguration().getAudioThumbnailMethod() > 0) {
                                    setThumb(CoverUtil.get().getThumbnailFromArtistAlbum(PMS.getConfiguration().getAudioThumbnailMethod() == 1 ? CoverUtil.AUDIO_AMAZON : CoverUtil.AUDIO_DISCOGS, audio.getArtist(), audio.getAlbum()));
                                }
                            }
                            if (!thumbOnly) {
                                audio.setAlbum(t.getFirst(FieldKey.ALBUM));
                                audio.setArtist(t.getFirst(FieldKey.ARTIST));
                                audio.setSongname(t.getFirst(FieldKey.TITLE));
                                String y = t.getFirst(FieldKey.YEAR);
                                try {
                                    if (y.length() > 4) {
                                        y = y.substring(0, 4);
                                    }
                                    audio.setYear(Integer.parseInt(((y != null && y.length() > 0) ? y : "0")));
                                    y = t.getFirst(FieldKey.TRACK);
                                    audio.setTrack(Integer.parseInt(((y != null && y.length() > 0) ? y : "1")));
                                    audio.setGenre(t.getFirst(FieldKey.GENRE));
                                } catch (Throwable e) {
                                    logger.debug("error in parsing unimportant metadata: " + e.getMessage());
                                }
                            }
                        }
                    } catch (Throwable e) {
                        logger.debug("Error in parsing audio file: " + e.getMessage() + " - " + (e.getCause() != null ? e.getCause().getMessage() : ""));
                        ffmpeg_parsing = false;
                    }
                    if (audio.getSongname() == null || audio.getSongname().length() == 0) {
                        audio.setSongname(f.getFile().getName());
                    }
                    if (!ffmpeg_parsing) {
                        getAudioCodes().add(audio);
                    }
                }
            }
            if (type == Format.IMAGE) {
                if (f.getFile() != null) {
                    try {
                        ffmpeg_parsing = false;
                        ImageInfo info = Sanselan.getImageInfo(f.getFile());
                        setWidth(info.getWidth());
                        setHeight(info.getHeight());
                        setBitsPerPixel(info.getBitsPerPixel());
                        String formatName = info.getFormatName();
                        if (formatName.startsWith("JPEG")) {
                            setCodecV("jpg");
                            IImageMetadata meta = Sanselan.getMetadata(f.getFile());
                            if (meta != null && meta instanceof JpegImageMetadata) {
                                JpegImageMetadata jpegmeta = (JpegImageMetadata) meta;
                                TiffField tf = jpegmeta.findEXIFValue(TiffConstants.EXIF_TAG_MODEL);
                                if (tf != null) {
                                    setModel(tf.getStringValue().trim());
                                }
                                tf = jpegmeta.findEXIFValue(TiffConstants.EXIF_TAG_EXPOSURE_TIME);
                                if (tf != null) {
                                    setExposure((int) (1000 * tf.getDoubleValue()));
                                }
                                tf = jpegmeta.findEXIFValue(TiffConstants.EXIF_TAG_ORIENTATION);
                                if (tf != null) {
                                    setOrientation(tf.getIntValue());
                                }
                                tf = jpegmeta.findEXIFValue(TiffConstants.EXIF_TAG_ISO);
                                if (tf != null) {
                                    setIso(tf.getIntValue());
                                }
                            }
                        } else if (formatName.startsWith("PNG")) {
                            setCodecV("png");
                        } else if (formatName.startsWith("GIF")) {
                            setCodecV("gif");
                        } else if (formatName.startsWith("TIF")) {
                            setCodecV("tiff");
                        }
                        setContainer(getCodecV());
                    } catch (Throwable e) {
                        logger.info("Error during the parsing of image with Sanselan... switching to Ffmpeg: " + e.getMessage());
                    }
                    try {
                        if (PMS.getConfiguration().getImageThumbnailsEnabled()) {
                            getImageMagickThumbnail(f);
                            String frameName = PMS.getConfiguration().getTempFolder() + "/imagemagick_thumbs/" + f.getFile().getName() + ".jpg";
                            File jpg = new File(frameName);
                            if (jpg.exists()) {
                                InputStream is = new FileInputStream(jpg);
                                int sz = is.available();
                                if (sz > 0) {
                                    setThumb(new byte[sz]);
                                    is.read(getThumb());
                                }
                                is.close();
                                if (!jpg.delete()) jpg.deleteOnExit();
                            }
                        }
                    } catch (Throwable e) {
                        logger.info("Error during the generating thumbnail of image with ImageMagick...: " + e.getMessage());
                    }
                }
            }
            if (ffmpeg_parsing) {
                if (!thumbOnly || !PMS.getConfiguration().isUseMplayerForVideoThumbs()) {
                    pw = getFFMpegThumbnail(f);
                }
                String input = "-";
                boolean dvrms = false;
                if (f.getFile() != null) {
                    input = ProcessUtil.getShortFileNameIfWideChars(f.getFile().getAbsolutePath());
                    dvrms = f.getFile().getAbsolutePath().toLowerCase().endsWith("dvr-ms");
                }
                if (!ffmpeg_failure && !thumbOnly) {
                    if (input.equals("-")) {
                        input = "pipe:";
                    }
                    boolean matchs = false;
                    ArrayList<String> lines = (ArrayList<String>) pw.getResults();
                    int langId = 0;
                    int subId = 0;
                    ListIterator<String> FFmpegMetaData = lines.listIterator();
                    for (String line : lines) {
                        FFmpegMetaData.next();
                        line = line.trim();
                        if (line.startsWith("Output")) {
                            matchs = false;
                        } else if (line.startsWith("Input")) {
                            if (line.indexOf(input) > -1) {
                                matchs = true;
                                setContainer(line.substring(10, line.indexOf(",", 11)).trim());
                            } else {
                                matchs = false;
                            }
                        } else if (matchs) {
                            if (line.indexOf("Duration") > -1) {
                                StringTokenizer st = new StringTokenizer(line, ",");
                                while (st.hasMoreTokens()) {
                                    String token = st.nextToken().trim();
                                    if (token.startsWith("Duration: ")) {
                                        String durationStr = token.substring(10);
                                        int l = durationStr.substring(durationStr.indexOf(".") + 1).length();
                                        if (l < 4) {
                                            durationStr = durationStr + "00".substring(0, 3 - l);
                                        }
                                        if (durationStr.indexOf("N/A") > -1) {
                                            setDuration(null);
                                        } else {
                                            setDuration(parseDurationString(durationStr));
                                        }
                                    } else if (token.startsWith("bitrate: ")) {
                                        String bitr = token.substring(9);
                                        int spacepos = bitr.indexOf(" ");
                                        if (spacepos > -1) {
                                            String value = bitr.substring(0, spacepos);
                                            String unit = bitr.substring(spacepos + 1);
                                            setBitrate(Integer.parseInt(value));
                                            if (unit.equals("kb/s")) {
                                                setBitrate(1024 * getBitrate());
                                            }
                                            if (unit.equals("mb/s")) {
                                                setBitrate(1048576 * getBitrate());
                                            }
                                        }
                                    }
                                }
                            } else if (line.indexOf("Audio:") > -1) {
                                StringTokenizer st = new StringTokenizer(line, ",");
                                int a = line.indexOf("(");
                                int b = line.indexOf("):", a);
                                DLNAMediaAudio audio = new DLNAMediaAudio();
                                if (langId == 0 && (getContainer().equals("avi") || getContainer().equals("ogm") || getContainer().equals("mov") || getContainer().equals("flv") || getContainer().equals("mp4"))) {
                                    langId++;
                                }
                                audio.setId(langId++);
                                if (a > -1 && b > a) {
                                    audio.setLang(line.substring(a + 1, b));
                                } else {
                                    audio.setLang(DLNAMediaLang.UND);
                                }
                                a = line.indexOf("[0x");
                                b = line.indexOf("]", a);
                                if (a > -1 && b > a + 3) {
                                    String idString = line.substring(a + 3, b);
                                    try {
                                        audio.setId(Integer.parseInt(idString, 16));
                                    } catch (NumberFormatException nfe) {
                                        logger.debug("Error in parsing Stream ID: " + idString);
                                    }
                                }
                                while (st.hasMoreTokens()) {
                                    String token = st.nextToken().trim();
                                    if (token.startsWith("Stream")) {
                                        audio.setCodecA(token.substring(token.indexOf("Audio: ") + 7));
                                    } else if (token.endsWith("Hz")) {
                                        audio.setSampleFrequency(token.substring(0, token.indexOf("Hz")).trim());
                                    } else if (token.equals("mono")) {
                                        audio.setNrAudioChannels(1);
                                    } else if (token.equals("stereo")) {
                                        audio.setNrAudioChannels(2);
                                    } else if (token.equals("5:1") || token.equals("5.1") || token.equals("6 channels")) {
                                        audio.setNrAudioChannels(6);
                                    } else if (token.equals("5 channels")) {
                                        audio.setNrAudioChannels(5);
                                    } else if (token.equals("4 channels")) {
                                        audio.setNrAudioChannels(4);
                                    } else if (token.equals("2 channels")) {
                                        audio.setNrAudioChannels(2);
                                    } else if (token.equals("s32")) {
                                        audio.setBitsperSample(32);
                                    } else if (token.equals("s24")) {
                                        audio.setBitsperSample(24);
                                    } else if (token.equals("s16")) {
                                        audio.setBitsperSample(16);
                                    }
                                }
                                int FFmpegMetaDataNr = FFmpegMetaData.nextIndex();
                                if (FFmpegMetaDataNr > -1) line = lines.get(FFmpegMetaDataNr);
                                if (line.indexOf("Metadata:") > -1) {
                                    FFmpegMetaDataNr = FFmpegMetaDataNr + 1;
                                    line = lines.get(FFmpegMetaDataNr);
                                    while (line.indexOf("      ") == 0) {
                                        if (line.toLowerCase().indexOf("title           :") > -1) {
                                            int aa = line.indexOf(": ");
                                            int bb = line.length();
                                            if (aa > -1 && bb > aa) {
                                                audio.setFlavor(line.substring(aa + 2, bb));
                                                break;
                                            }
                                        } else {
                                            FFmpegMetaDataNr = FFmpegMetaDataNr + 1;
                                            line = lines.get(FFmpegMetaDataNr);
                                        }
                                    }
                                }
                                getAudioCodes().add(audio);
                            } else if (line.indexOf("Video:") > -1) {
                                StringTokenizer st = new StringTokenizer(line, ",");
                                while (st.hasMoreTokens()) {
                                    String token = st.nextToken().trim();
                                    if (token.startsWith("Stream")) {
                                        setCodecV(token.substring(token.indexOf("Video: ") + 7));
                                    } else if ((token.indexOf("tbc") > -1 || token.indexOf("tb(c)") > -1)) {
                                        String frameRateDoubleString = token.substring(0, token.indexOf("tb")).trim();
                                        try {
                                            if (!frameRateDoubleString.equals(getFrameRate())) {
                                                Double frameRateDouble = Double.parseDouble(frameRateDoubleString);
                                                setFrameRate(String.format(Locale.ENGLISH, "%.2f", frameRateDouble / 2));
                                            }
                                        } catch (NumberFormatException nfe) {
                                        }
                                    } else if ((token.indexOf("tbr") > -1 || token.indexOf("tb(r)") > -1) && getFrameRate() == null) {
                                        setFrameRate(token.substring(0, token.indexOf("tb")).trim());
                                    } else if ((token.indexOf("fps") > -1 || token.indexOf("fps(r)") > -1) && getFrameRate() == null) {
                                        setFrameRate(token.substring(0, token.indexOf("fps")).trim());
                                    } else if (token.indexOf("x") > -1) {
                                        String resolution = token.trim();
                                        if (resolution.indexOf(" [") > -1) {
                                            resolution = resolution.substring(0, resolution.indexOf(" ["));
                                        }
                                        try {
                                            setWidth(Integer.parseInt(resolution.substring(0, resolution.indexOf("x"))));
                                            setHeight(Integer.parseInt(resolution.substring(resolution.indexOf("x") + 1)));
                                        } catch (NumberFormatException nfe) {
                                        }
                                    }
                                }
                            } else if (line.indexOf("Subtitle:") > -1 && !line.contains("tx3g")) {
                                DLNAMediaSubtitle lang = new DLNAMediaSubtitle();
                                lang.setType((line.contains("dvdsub") && Platform.isWindows() ? DLNAMediaSubtitle.VOBSUB : DLNAMediaSubtitle.EMBEDDED));
                                int a = line.indexOf("(");
                                int b = line.indexOf("):", a);
                                if (a > -1 && b > a) {
                                    lang.setLang(line.substring(a + 1, b));
                                } else {
                                    lang.setLang(DLNAMediaLang.UND);
                                }
                                lang.setId(subId++);
                                int FFmpegMetaDataNr = FFmpegMetaData.nextIndex();
                                if (FFmpegMetaDataNr > -1) line = lines.get(FFmpegMetaDataNr);
                                if (line.indexOf("Metadata:") > -1) {
                                    FFmpegMetaDataNr = FFmpegMetaDataNr + 1;
                                    line = lines.get(FFmpegMetaDataNr);
                                    while (line.indexOf("      ") == 0) {
                                        if (line.toLowerCase().indexOf("title           :") > -1) {
                                            int aa = line.indexOf(": ");
                                            int bb = line.length();
                                            if (aa > -1 && bb > aa) {
                                                lang.setFlavor(line.substring(aa + 2, bb));
                                                break;
                                            }
                                        } else {
                                            FFmpegMetaDataNr = FFmpegMetaDataNr + 1;
                                            line = lines.get(FFmpegMetaDataNr);
                                        }
                                    }
                                }
                                getSubtitlesCodes().add(lang);
                            }
                        }
                    }
                }
                if (!thumbOnly && getContainer() != null && f.getFile() != null && getContainer().equals("mpegts") && isH264() && getDurationInSeconds() == 0) {
                    try {
                        int length = MpegUtil.getDurationFromMpeg(f.getFile());
                        if (length > 0) {
                            setDuration((double) length);
                        }
                    } catch (IOException e) {
                        logger.trace("Error in retrieving length: " + e.getMessage());
                    }
                }
                if (PMS.getConfiguration().isUseMplayerForVideoThumbs() && type == Format.VIDEO && !dvrms) {
                    try {
                        getMplayerThumbnail(f);
                        String frameName = "" + f.hashCode();
                        frameName = PMS.getConfiguration().getTempFolder() + "/mplayer_thumbs/" + frameName + "00000001/00000001.jpg";
                        frameName = frameName.replace(',', '_');
                        File jpg = new File(frameName);
                        if (jpg.exists()) {
                            InputStream is = new FileInputStream(jpg);
                            int sz = is.available();
                            if (sz > 0) {
                                setThumb(new byte[sz]);
                                is.read(getThumb());
                            }
                            is.close();
                            if (!jpg.delete()) {
                                jpg.deleteOnExit();
                            }
                            if (!jpg.getParentFile().delete()) {
                                jpg.getParentFile().delete();
                            }
                        }
                    } catch (IOException e) {
                    }
                }
                if (type == Format.VIDEO && pw != null && getThumb() == null) {
                    InputStream is;
                    try {
                        is = pw.getInputStream(0);
                        int sz = is.available();
                        if (sz > 0) {
                            setThumb(new byte[sz]);
                            is.read(getThumb());
                        }
                        is.close();
                        if (sz > 0 && !java.awt.GraphicsEnvironment.isHeadless()) {
                            BufferedImage image = ImageIO.read(new ByteArrayInputStream(getThumb()));
                            if (image != null) {
                                Graphics g = image.getGraphics();
                                g.setColor(Color.WHITE);
                                g.setFont(new Font("Arial", Font.PLAIN, 14));
                                int low = 0;
                                if (getWidth() > 0) {
                                    if (getWidth() == 1920 || getWidth() == 1440) {
                                        g.drawString("1080p", 0, low += 18);
                                    } else if (getWidth() == 1280) {
                                        g.drawString("720p", 0, low += 18);
                                    }
                                }
                                ByteArrayOutputStream out = new ByteArrayOutputStream();
                                ImageIO.write(image, "jpeg", out);
                                setThumb(out.toByteArray());
                            }
                        }
                    } catch (IOException e) {
                        logger.debug("Error while decoding thumbnail : " + e.getMessage());
                    }
                }
            }
            finalize(type, f);
            setMediaparsed(true);
        }
    }
}
