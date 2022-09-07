class BackupThread extends Thread {
    protected void doSetLastModifiedTime(final long modtime) throws Exception {
        final ChannelSftp channel = fileSystem.getChannel();
        try {
            int newMTime = (int) (modtime / 1000L);
            attrs.setACMODTIME(attrs.getATime(), newMTime);
            channel.setStat(relPath, attrs);
        } finally {
            fileSystem.putChannel(channel);
        }
    }
}
