class BackupThread extends Thread {
    @Override
    public void parse(InputSource input, NormaliserResults results, boolean convertOnly) throws IOException, SAXException {
        try {
            if (!(input instanceof XenaInputSource)) {
                throw new XenaException("Can only normalise XenaInputSource objects.");
            }
            XenaInputSource xis = (XenaInputSource) input;
            JorbisAudioFileReader vorbisReader = new JorbisAudioFileReader();
            AudioInputStream audioIS;
            if (xis.getFile() == null) {
                audioIS = vorbisReader.getAudioInputStream(xis.getByteStream());
            } else {
                audioIS = vorbisReader.getAudioInputStream(xis.getFile());
            }
            AudioFormat sourceFormat = audioIS.getFormat();
            InputStream flacStream;
            File tmpFlacFile = null;
            if (sourceFormat.getEncoding().toString().equals("FLAC")) {
                flacStream = xis.getByteStream();
            } else {
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * nSampleSizeInBits / 8, sourceFormat.getSampleRate(), bBigEndian);
                JorbisFormatConversionProvider vorbisConverter = new JorbisFormatConversionProvider();
                AudioInputStream rawIS = vorbisConverter.getAudioInputStream(targetFormat, audioIS);
                AudioFormat rawFormat = rawIS.getFormat();
                String endianStr = rawFormat.isBigEndian() ? "big" : "little";
                tmpFlacFile = File.createTempFile("flacoutput", ".tmp");
                tmpFlacFile.deleteOnExit();
                PluginManager pluginManager = normaliserManager.getPluginManager();
                PropertiesManager propManager = pluginManager.getPropertiesManager();
                String flacEncoderProg = propManager.getPropertyValue(AudioProperties.AUDIO_PLUGIN_NAME, AudioProperties.FLAC_LOCATION_PROP_NAME);
                if (flacEncoderProg == null || flacEncoderProg.equals("")) {
                    throw new IOException("Cannot find the flac executable. Please check its location in the audio plugin settings.");
                }
                String signStr;
                Encoding encodingType = rawFormat.getEncoding();
                if (encodingType.equals(Encoding.PCM_SIGNED)) {
                    signStr = "signed";
                } else if (encodingType.equals(Encoding.PCM_UNSIGNED)) {
                    signStr = "unsigned";
                } else {
                    throw new IOException("Invalid raw encoding type: " + encodingType);
                }
                List<String> commandList = new ArrayList<String>();
                commandList.add(flacEncoderProg);
                commandList.add("--output-name");
                commandList.add(tmpFlacFile.getAbsolutePath());
                commandList.add("--force");
                commandList.add("--endian");
                commandList.add(endianStr);
                commandList.add("--channels");
                commandList.add(String.valueOf(rawFormat.getChannels()));
                commandList.add("--sample-rate");
                commandList.add(String.valueOf(rawFormat.getSampleRate()));
                commandList.add("--sign");
                commandList.add(signStr);
                commandList.add("--bps");
                commandList.add(String.valueOf(rawFormat.getSampleSizeInBits()));
                commandList.add("-");
                String[] commandArr = commandList.toArray(new String[0]);
                Process pr;
                final StringBuilder errorBuff = new StringBuilder();
                try {
                    pr = Runtime.getRuntime().exec(commandArr);
                    final InputStream eis = pr.getErrorStream();
                    final InputStream ois = pr.getInputStream();
                    Thread et = new Thread() {

                        @Override
                        public void run() {
                            try {
                                int c;
                                while (0 <= (c = eis.read())) {
                                    errorBuff.append((char) c);
                                }
                            } catch (IOException x) {
                            }
                        }
                    };
                    et.start();
                    Thread ot = new Thread() {

                        @Override
                        public void run() {
                            int c;
                            try {
                                while (0 <= (c = ois.read())) {
                                    System.err.print((char) c);
                                }
                            } catch (IOException x) {
                            }
                        }
                    };
                    ot.start();
                    OutputStream procOS = new BufferedOutputStream(pr.getOutputStream());
                    byte[] buffer = new byte[10 * 1024];
                    int bytesRead;
                    while (0 < (bytesRead = rawIS.read(buffer))) {
                        procOS.write(buffer, 0, bytesRead);
                    }
                    procOS.flush();
                    procOS.close();
                    pr.waitFor();
                } catch (Exception flacEx) {
                    throw new IOException("An error occured in the flac normaliser. Please ensure you are using Flac version 1.2.1 or later." + flacEx);
                }
                if (pr.exitValue() == 1) {
                    throw new IOException("An error occured in the flac normaliser. Please ensure you are using Flac version 1.2.1 or later." + errorBuff);
                }
                flacStream = new FileInputStream(tmpFlacFile);
            }
            if (convertOnly) {
                FileUtils.fileCopy(flacStream, results.getDestinationDirString() + File.separator + results.getOutputFileName(), true);
            } else {
                ContentHandler ch = getContentHandler();
                AttributesImpl att = new AttributesImpl();
                ch.startElement(AUDIO_URI, FLAC_TAG, AUDIO_PREFIX + ":" + FLAC_TAG, att);
                InputStreamEncoder.base64Encode(flacStream, ch);
                ch.endElement(AUDIO_URI, FLAC_TAG, AUDIO_PREFIX + ":" + FLAC_TAG);
            }
            flacStream.close();
            setExportedChecksum(generateChecksum(tmpFlacFile));
            if (tmpFlacFile != null) {
                tmpFlacFile.delete();
            }
        } catch (XenaException x) {
            throw new SAXException(x);
        } catch (UnsupportedAudioFileException e) {
            throw new IOException("Xena does not handle this particular audio format. " + e);
        }
    }
}
