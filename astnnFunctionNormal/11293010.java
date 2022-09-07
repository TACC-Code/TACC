class BackupThread extends Thread {
    protected void copy(InputStream rawin, Socket4 sock, boolean update) throws IOException {
        BufferedInputStream in = new BufferedInputStream(rawin);
        byte[] buffer = new byte[BlobImpl.COPYBUFFER_LENGTH];
        int bytesread = -1;
        while ((bytesread = rawin.read(buffer)) >= 0) {
            sock.write(buffer, 0, bytesread);
            if (update) {
                _currentByte += bytesread;
            }
        }
        in.close();
    }
}
