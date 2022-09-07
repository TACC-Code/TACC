class BackupThread extends Thread {
    public void transfer(OutputStream out, int length) throws IOException {
        int buffSize = (length < MAX_TRANSFER_BUFFER_SIZE) ? length : MAX_TRANSFER_BUFFER_SIZE;
        byte[] buffer = new byte[buffSize];
        int read;
        while ((read = in.read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
    }
}
