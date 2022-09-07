class BackupThread extends Thread {
    @Override
    public Document parse(String uri) throws SAXException, IOException {
        if ((null == uri) || (uri.length() <= 0)) throw new IOException("parse() no " + URI.class.getSimpleName() + " string provided");
        InputStream in = null;
        try {
            final URL url = new URI(uri).toURL();
            in = url.openStream();
            return parse(in);
        } catch (URISyntaxException e) {
            throw new StreamCorruptedException("parse(" + uri + ") " + e.getClass().getName() + ": " + e.getMessage());
        } finally {
            FileUtil.closeAll(in);
        }
    }
}
