class BackupThread extends Thread {
    private void statSelf() throws Exception {
        ChannelSftp channel = fileSystem.getChannel();
        try {
            setStat(channel.stat(relPath));
        } catch (final SftpException e) {
            try {
                if (e.id != ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                    channel.disconnect();
                    channel = fileSystem.getChannel();
                    setStat(channel.stat(relPath));
                } else {
                    attrs = null;
                }
            } catch (final SftpException e2) {
                attrs = null;
            }
        } finally {
            fileSystem.putChannel(channel);
        }
    }
}
