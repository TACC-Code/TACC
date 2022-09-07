class BackupThread extends Thread {
    protected void copy(Socket4 sock, OutputStream rawout, int length, boolean update) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(rawout);
        byte[] buffer = new byte[BlobImpl.COPYBUFFER_LENGTH];
        int totalread = 0;
        while (totalread < length) {
            int stilltoread = length - totalread;
            int readsize = (stilltoread < buffer.length ? stilltoread : buffer.length);
            int curread = sock.read(buffer, 0, readsize);
            if (curread < 0) {
                throw new IOException();
            }
            out.write(buffer, 0, curread);
            totalread += curread;
            if (update) {
                _currentByte += curread;
            }
        }
        out.flush();
        out.close();
    }
}
