class BackupThread extends Thread {
    public static void dumpAndClose(InputStream istream, OutputStream ostream) throws IOException {
        int read;
        while ((read = istream.read()) != -1) {
            ostream.write((byte) read);
        }
        istream.close();
        ostream.close();
    }
}
