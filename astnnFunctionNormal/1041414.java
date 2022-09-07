class BackupThread extends Thread {
    private void copy(InputStream in, PrintStream out) throws IOException {
        byte[] buffer = new byte[256];
        int len;
        while ((len = in.read(buffer)) > -1) out.write(buffer, 0, len);
    }
}
