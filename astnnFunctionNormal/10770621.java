class BackupThread extends Thread {
    private int getStream(final InputStream is, OutputStream out) throws IOException {
        int progress = 0;
        byte[] buffer = new byte[1024 * 8];
        int readCount;
        while ((readCount = is.read(buffer)) > 0) {
            String bufStr = new String(buffer);
            System.out.println(bufStr);
            out.write(buffer, 0, readCount);
            progress += readCount;
        }
        out.flush();
        out.close();
        out = null;
        return progress;
    }
}
