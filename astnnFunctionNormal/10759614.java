class BackupThread extends Thread {
    protected ChannelSftp getChannel() throws IOException {
        if (this.session == null) {
            Session session;
            UserAuthenticationData authData = null;
            try {
                final GenericFileName rootName = (GenericFileName) getRootName();
                authData = UserAuthenticatorUtils.authenticate(getFileSystemOptions(), SftpFileProvider.AUTHENTICATOR_TYPES);
                session = SftpClientFactory.createConnection(rootName.getHostName(), rootName.getPort(), UserAuthenticatorUtils.getData(authData, UserAuthenticationData.USERNAME, UserAuthenticatorUtils.toChar(rootName.getUserName())), UserAuthenticatorUtils.getData(authData, UserAuthenticationData.PASSWORD, UserAuthenticatorUtils.toChar(rootName.getPassword())), getFileSystemOptions());
            } catch (final Exception e) {
                throw new FileSystemException("vfs.provider.sftp/connect.error", getRootName(), e);
            } finally {
                UserAuthenticatorUtils.cleanup(authData);
            }
            this.session = session;
        }
        try {
            final ChannelSftp channel;
            if (idleChannel != null) {
                channel = idleChannel;
                idleChannel = null;
            } else {
                channel = (ChannelSftp) session.openChannel("sftp");
                channel.connect();
                Boolean userDirIsRoot = SftpFileSystemConfigBuilder.getInstance().getUserDirIsRoot(getFileSystemOptions());
                String workingDirectory = getRootName().getPath();
                if (workingDirectory != null && (userDirIsRoot == null || !userDirIsRoot.booleanValue())) {
                    try {
                        channel.cd(workingDirectory);
                    } catch (SftpException e) {
                        throw new FileSystemException("vfs.provider.sftp/change-work-directory.error", workingDirectory);
                    }
                }
            }
            return channel;
        } catch (final JSchException e) {
            throw new FileSystemException("vfs.provider.sftp/connect.error", getRootName(), e);
        }
    }
}
