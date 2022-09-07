class BackupThread extends Thread {
    protected void doDelete() throws Exception {
        final ChannelSftp channel = fileSystem.getChannel();
        try {
            if (getType() == FileType.FILE) {
                channel.rm(relPath);
            } else {
                channel.rmdir(relPath);
            }
        } finally {
            fileSystem.putChannel(channel);
        }
    }
}
