class BackupThread extends Thread {
    protected void doRename(FileObject newfile) throws Exception {
        final ChannelSftp channel = fileSystem.getChannel();
        try {
            channel.rename(relPath, ((SftpFileObject) newfile).relPath);
        } finally {
            fileSystem.putChannel(channel);
        }
    }
}
