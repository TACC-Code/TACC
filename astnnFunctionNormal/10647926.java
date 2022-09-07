class BackupThread extends Thread {
    public static LookAheadBuffer createBuffer(URL url) throws IOException {
        final Reader isr = new InputStreamReader(url.openStream());
        final BufferedReader bufferedReader = new BufferedReader(isr);
        return new LookAheadBufferImpl(bufferedReader);
    }
}
