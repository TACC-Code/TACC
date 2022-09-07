class BackupThread extends Thread {
    public static void readUrl(URL url, StringBuilder strb, char[] cbuf) throws IOException {
        Reader in = new InputStreamReader(url.openStream());
        int rlen;
        while ((rlen = in.read(cbuf)) != -1) {
            strb.append(cbuf, 0, rlen);
        }
        in.close();
    }
}
