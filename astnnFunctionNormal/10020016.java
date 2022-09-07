class BackupThread extends Thread {
    protected InputStream doGetInputStream() throws Exception {
        synchronized (fileSystem) {
            final ChannelSftp channel = fileSystem.getChannel();
            try {
                InputStream is;
                try {
                    if (!getType().hasContent()) {
                        throw new FileSystemException("vfs.provider/read-not-file.error", getName());
                    }
                    is = channel.get(relPath);
                } catch (SftpException e) {
                    if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                    }
                    throw new FileSystemException(e);
                }
                return new SftpInputStream(channel, is);
            } finally {
            }
        }
    }
}
