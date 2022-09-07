class BackupThread extends Thread {
    public void parse(URL url, ParserHandler handler) throws ParserException, IOException {
        parse(new InputStreamReader(url.openStream()), handler);
    }
}
