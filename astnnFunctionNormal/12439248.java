class BackupThread extends Thread {
    public void write(InputStream in) throws IOException {
        int howManyBytes;
        byte[] bytesIn = new byte[2048];
        while ((howManyBytes = in.read(bytesIn)) >= 0) write(bytesIn, 0, howManyBytes);
    }
}
