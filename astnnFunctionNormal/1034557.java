class BackupThread extends Thread {
    public Document buildDocument() {
        String uri = this.getUri();
        if (uri == null) {
            throw new RunException("URL为空，不能构建document");
        }
        Document document = null;
        Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);
        InputStream in = null;
        try {
            UserAgentContext uacontext = new SimpleUserAgentContext();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Snap.LOG.info("URL:" + uri);
            URL url = new URL(uri);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Baiduspider+(+http://www.baidu.com/search/spider.htm)");
            in = connection.getInputStream();
            document = builder.newDocument();
            String charset = org.lobobrowser.util.Urls.getCharset(connection);
            if (null != charset) {
                if (!charset.toUpperCase().startsWith("UTF")) {
                    charset = "GB2312";
                }
            }
            Reader reader = new InputStreamReader(in, charset);
            HtmlParser parser = new HtmlParser(uacontext, document, new SnapParserErrorHandler(), uri, uri);
            parser.parse(reader);
            return document;
        } catch (Exception e) {
            if (LOG.isInfoEnabled()) {
                LOG.info(e.getMessage());
            } else {
                LOG.error(e.getMessage(), e);
            }
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                in = null;
            }
        }
        return document;
    }
}
