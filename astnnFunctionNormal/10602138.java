class BackupThread extends Thread {
    public void close() {
        RandomAccessFile appender = null;
        try {
            if (metaPosition > 0) {
                long oldPos = channel.position();
                try {
                    log.debug("In the metadata writing (close) method - duration:{}", duration);
                    channel.position(metaPosition);
                    writeMetadataTag(duration * 0.001, videoCodecId, audioCodecId);
                } finally {
                    channel.position(oldPos);
                }
            }
            channel.close();
            fos.close();
            channel = null;
            if (append) {
                appender = new RandomAccessFile(file, "rw");
                channel = appender.getChannel();
                channel.position(13);
                writeMetadataTag(duration * 0.001, videoCodecId, audioCodecId);
            }
        } catch (IOException e) {
            log.error("IO error on close", e);
        } finally {
            try {
                if (appender != null) {
                    appender.close();
                }
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }
    }
}
