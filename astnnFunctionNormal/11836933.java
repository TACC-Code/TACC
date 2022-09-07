class BackupThread extends Thread {
    @Override
    protected long position() throws IOException {
        return fis.getChannel().position();
    }
}
