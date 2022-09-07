class BackupThread extends Thread {
    private byte[] getRawBytes(URL url) throws IOException {
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] tmp = new byte[1024];
        int bytes;
        while ((bytes = in.read(tmp)) != -1) {
            out.write(tmp, 0, bytes);
        }
        return out.toByteArray();
    }
}
