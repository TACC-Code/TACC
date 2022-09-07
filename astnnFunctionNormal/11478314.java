class BackupThread extends Thread {
    public Map<String, String> extractMediaContainer(String location) throws CannotExtractMetaDataException {
        Map<String, String> properties = new HashMap<String, String>();
        try {
            IContainer container = IContainer.make();
            if (container.open(location, IContainer.Type.READ, null) < 0) {
                throw new CannotExtractMetaDataException("could not open file: " + location);
            }
            if (container.queryStreamMetaData() < 0) {
                throw new CannotExtractMetaDataException("couldn't query stream meta data for some reason...");
            }
            for (int i = 0; i < container.getNumProperties(); i++) {
                IProperty prop = container.getPropertyMetaData(i);
                properties.put(prop.getName(), container.getPropertyAsString(prop.getName()));
            }
            properties.put("streams", String.valueOf(container.getNumStreams()));
            if (container.getDuration() == Global.NO_PTS) {
                properties.put("duration", String.valueOf(container.getDuration()));
            } else {
                properties.put("duration", String.valueOf(container.getDuration() / 1000));
            }
            if (container.getStartTime() == Global.NO_PTS) {
                properties.put("startTime", String.valueOf(container.getStartTime()));
            } else {
                properties.put("startTime", String.valueOf(container.getStartTime() / 1000));
            }
            properties.put("bitrate", String.valueOf(container.getBitRate()));
            for (String meta : container.getMetaData().getKeys()) {
                properties.put("container." + meta, container.getMetaData().getValue(meta));
            }
            for (int i = 0; i < container.getNumStreams(); i++) {
                IStream stream = container.getStream(i);
                IStreamCoder coder = stream.getStreamCoder();
                for (String meta : stream.getMetaData().getKeys()) {
                    properties.put("stream." + i + ".meta." + meta, stream.getMetaData().getValue(meta));
                }
                properties.put("stream." + i + ".type", coder.getCodecType().name());
                properties.put("stream." + i + ".codec", coder.getCodecID().name());
                properties.put("stream." + i + ".duration", String.valueOf(stream.getDuration()));
                if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO) {
                    properties.put("stream." + i + ".sampleRate", String.valueOf(coder.getSampleRate()));
                    properties.put("stream." + i + ".channels", String.valueOf(coder.getChannels()));
                    properties.put("stream." + i + ".format", coder.getSampleFormat().name());
                } else if (coder.getCodecType() == ICodec.Type.CODEC_TYPE_VIDEO) {
                    properties.put("stream." + i + ".width", String.valueOf(coder.getWidth()));
                    properties.put("stream." + i + ".height", String.valueOf(coder.getHeight()));
                    properties.put("stream." + i + ".format", coder.getPixelType().name());
                    properties.put("stream." + i + ".frameRate", String.valueOf(coder.getFrameRate().getDouble()));
                }
            }
        } catch (UnsatisfiedLinkError ex) {
            LOG.log(Level.SEVERE, "Could not extract meta data. {0}", ex.getMessage());
        } catch (NoClassDefFoundError ex) {
            LOG.log(Level.SEVERE, "Could not extract meta data. {0}", ex.getMessage());
        }
        return properties;
    }
}
