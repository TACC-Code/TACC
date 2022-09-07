class BackupThread extends Thread {
    public void seek(long position) throws IOException {
        fis.getChannel().position(position);
    }
}
