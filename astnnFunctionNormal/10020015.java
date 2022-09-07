class BackupThread extends Thread {
    InputStream getInputStream(long filePointer) throws IOException {
        final ChannelSftp channel = fileSystem.getChannel();
        try {
            ByteArrayOutputStream outstr = new ByteArrayOutputStream();
            try {
                channel.get(getName().getPathDecoded(), outstr, null, ChannelSftp.RESUME, filePointer);
            } catch (SftpException e) {
                throw new FileSystemException(e);
            }
            outstr.close();
            return new ByteArrayInputStream(outstr.toByteArray());
        } finally {
            fileSystem.putChannel(channel);
        }
    }
}
