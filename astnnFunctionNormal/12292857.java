class BackupThread extends Thread {
    private String streamAsString(InputStream is) throws IOException {
        final ByteArrayOutputStream s = new ByteArrayOutputStream();
        final byte b[] = new byte[1024];
        int read;
        while ((read = is.read(b)) >= 0) s.write(b, 0, read);
        is.close();
        return s.toString("ASCII");
    }
}
