class BackupThread extends Thread {
    protected void doCreateFolder() throws Exception {
        final ChannelSftp channel = fileSystem.getChannel();
        try {
            channel.mkdir(relPath);
        } finally {
            fileSystem.putChannel(channel);
        }
    }
}
