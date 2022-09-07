class BackupThread extends Thread {
    public void run() {
        int ch;
        byte[] buffer = new byte[512];
        int bytes_read;
        try {
            while (continueProcessing) {
                bytes_read = pi.read(buffer);
                if (bytes_read == -1) {
                    debug.print("Closing Streams");
                    pi.close();
                    po.close();
                    return;
                }
                po.write(buffer, 0, bytes_read);
                if (copyStream != null && !stopPiping) {
                    copyStream.write(buffer, 0, bytes_read);
                    copyStream.flush();
                }
                po.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}
