class BackupThread extends Thread {
    private void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[256];
        int read = is.read(buffer, 0, buffer.length);
        while (read > -1) {
            os.write(buffer, 0, read);
            read = is.read(buffer, 0, buffer.length);
        }
    }
}
