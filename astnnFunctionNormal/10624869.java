class BackupThread extends Thread {
    public void readData(MultipartFormField field, DelimitedBufferedInputStream in, byte[] delim) throws IOException {
        this.field = field;
        OutputStream out = null;
        byte[] buff = new byte[BUFSIZE];
        int count;
        try {
            out = new BufferedOutputStream(new FileOutputStream(getFile()));
            while ((count = in.readToDelimiter(buff, 0, buff.length, delim)) > 0) out.write(buff, 0, count);
            if (count == -1) throw new IOException("Didn't find boundary whilst reading field data");
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (Exception e) {
                ;
            }
        }
    }
}
