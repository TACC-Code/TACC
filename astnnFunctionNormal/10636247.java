class BackupThread extends Thread {
    public void parseURL(URL url) throws IOException {
        if (!url.toString().endsWith(".html")) throw new IllegalArgumentException("must end in .html");
        varAttrs = new HashMap();
        varAttrsData = new HashMap();
        recDims = new HashMap();
        InputStream in = url.openStream();
        new ParserDelegator().parse(new InputStreamReader(in), new MyCallBack(), true);
        in.close();
    }
}
