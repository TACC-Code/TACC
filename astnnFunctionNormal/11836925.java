class BackupThread extends Thread {
    public boolean eof() throws IOException {
        return file.length() == fis.getChannel().position();
    }
}
