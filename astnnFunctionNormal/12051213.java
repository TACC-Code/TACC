class BackupThread extends Thread {
    protected static String getURLContents(URL url) throws IOException {
        Reader r = new InputStreamReader(url.openStream());
        StringBuffer sb = new StringBuffer();
        int read = r.read();
        while (read != -1) {
            sb.append((char) read);
            read = r.read();
        }
        return sb.toString();
    }
}
