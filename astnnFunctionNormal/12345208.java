class BackupThread extends Thread {
    public void sendFileContent() throws IOException, InterruptedException {
        int filesize = (int) file.length();
        byte[] buf = new byte[2048];
        InputStream in = null;
        if (this.file instanceof StreamingFile) in = ((StreamingFile) this.file).getInputStream(); else in = new FileInputStream(this.file);
        Thread currentThread = Thread.currentThread();
        int readlen;
        while ((readlen = in.read(buf, 3, 2045)) > 0) {
            buf[0] = 0;
            buf[1] = (byte) ((readlen >> 0) & 0xff);
            buf[2] = (byte) ((readlen >> 8) & 0xff);
            out.write(buf, 0, readlen + 3);
            offset += readlen;
            out.flush();
            if (currentThread.isInterrupted()) {
                in.close();
                throw new InterruptedException("thread interrupted");
            }
        }
        in.close();
        out.write(0);
        out.write(0);
        out.write(0);
    }
}
