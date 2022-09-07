class BackupThread extends Thread {
    public Version getFileVersion() throws IOException {
        File versionFile = new File(rootDir, FileConventions.VERSION_FILE);
        FileChannel channel = new FileInputStream(versionFile).getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(64);
        buffer.limit(FileConventions.VERSION_PREFIX.length());
        channelHelper.readRemaining(channel, buffer);
        buffer.flip();
        String prefix = new String(buffer.array(), 0, buffer.limit());
        validator.isTrue(prefix.equals(FileConventions.VERSION_PREFIX), "expected prefix \"" + FileConventions.VERSION_PREFIX + "\" not found");
        buffer.clear().limit(8);
        channelHelper.readRemaining(channel, buffer);
        channel.close();
        buffer.flip();
        return new Version(buffer.getLong());
    }
}
