class BackupThread extends Thread {
    public static void copyPipe(InputStream in, OutputStream out, int bufSizeHint) throws IOException {
        int read = -1;
        byte[] buf = new byte[bufSizeHint];
        while ((read = in.read(buf, 0, bufSizeHint)) >= 0) {
            out.write(buf, 0, read);
        }
        out.flush();
    }
}
