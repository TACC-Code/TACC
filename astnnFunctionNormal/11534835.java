class BackupThread extends Thread {
    public ParseCpsl(URL url, String encoding, HashMap existingMacros) throws IOException {
        this(new InputStreamReader(new BufferedInputStream(url.openStream()), encoding));
        macrosMap = existingMacros;
        baseURL = url;
        this.encoding = encoding;
    }
}
