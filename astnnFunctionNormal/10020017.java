class BackupThread extends Thread {
    protected OutputStream doGetOutputStream(boolean bAppend) throws Exception {
        final ChannelSftp channel = fileSystem.getChannel();
        return new SftpOutputStream(channel, channel.put(relPath));
    }
}
