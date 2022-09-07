class BackupThread extends Thread {
    public byte[] getData() {
        File file = getFile();
        if (file == null) return new byte[0];
        InputStream in = null;
        ByteArrayOutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            out = new ByteArrayOutputStream();
            byte[] buff = new byte[BUFSIZE];
            int count;
            while ((count = in.read(buff, 0, buff.length)) > 0) out.write(buff, 0, count);
            return out.toByteArray();
        } catch (IOException e) {
            throw new FormDataAdaptorException("Couldn't retreive the data from the file", e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
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
