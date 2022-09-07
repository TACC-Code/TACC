class BackupThread extends Thread {
    public void printInputStreamToout(InputStream teststream, OutputStream out) throws IOException {
        byte buf[] = new byte[1024];
        int len;
        while ((len = teststream.read(buf)) > 0) out.write(buf, 0, len);
        out.close();
    }
}
