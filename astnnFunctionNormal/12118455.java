class BackupThread extends Thread {
    public static void streamContent(OutputStream out, InputStream in, int length) throws IOException {
        int bufferSize = 512;
        if (out == null) {
            throw new IOException("Attempt to write to null output stream");
        }
        if (in == null) {
            throw new IOException("Attempt to read from null input stream");
        }
        if (length == 0) {
            throw new IOException("Attempt to write 0 bytes of content to output stream");
        }
        BufferedOutputStream bos = new BufferedOutputStream(out, bufferSize);
        BufferedInputStream bis = new BufferedInputStream(in, bufferSize);
        byte[] buffer = new byte[length];
        int read = 0;
        try {
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, read);
            }
        } catch (IOException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Problem reading/writing buffers", e);
            }
            bis.close();
            bos.close();
            throw e;
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (bos != null) {
                bos.flush();
                bos.close();
            }
        }
    }
}
