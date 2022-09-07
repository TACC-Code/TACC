class BackupThread extends Thread {
    protected byte[] readData(InputStream in) throws IOException {
        int c = -1;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        while ((c = in.read()) != -1) bout.write(c);
        return bout.toByteArray();
    }
}
