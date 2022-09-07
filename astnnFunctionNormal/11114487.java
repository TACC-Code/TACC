class BackupThread extends Thread {
    public void write(BufferedInputStream istream, BufferedOutputStream ostream) throws IOException {
        int available = (istream.available()) <= 0 ? DEFAULT_BUFFER_SIZE : istream.available();
        int chunkSize = Math.min(DEFAULT_BUFFER_SIZE, available);
        byte[] readBuffer = new byte[chunkSize];
        int n = istream.read(readBuffer);
        while (n > 0) {
            ostream.write(readBuffer, 0, n);
            n = istream.read(readBuffer);
        }
    }
}
