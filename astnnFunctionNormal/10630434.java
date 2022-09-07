class BackupThread extends Thread {
    private static void copyErr(InputStream err, byte[] buf) throws IOException {
        int av;
        while ((av = err.available()) > 0) {
            System.err.write(buf, 0, err.read(buf, 0, Math.min(av, buf.length)));
        }
    }
}
