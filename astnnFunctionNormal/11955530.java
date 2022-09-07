class BackupThread extends Thread {
    @Override
    public ReadableByteChannel getChannel() throws IOException {
        return NioUtils.getChannel(getStream());
    }
}
