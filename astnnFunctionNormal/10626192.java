class BackupThread extends Thread {
    public void configureJSON(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count;
        while ((count = in.read(buffer)) != -1) out.write(buffer, 0, count);
        configureJSON(out.toString());
    }
}
