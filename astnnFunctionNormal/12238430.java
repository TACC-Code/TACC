class BackupThread extends Thread {
    private byte[] readStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int read;
        while ((read = in.read(buf)) > -1) {
            out.write(buf, 0, read);
        }
        out.flush();
        return out.toByteArray();
    }
}
