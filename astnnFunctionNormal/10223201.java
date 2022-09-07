class BackupThread extends Thread {
    public void parse() throws ParserConfigurationException, SAXException, IOException {
        URLConnection urlConnection = null;
        InputStream urlInputStream = null;
        SAXParserFactory spf = null;
        SAXParser sp = null;
        URL url = new URL(this.urlString);
        if ((urlConnection = url.openConnection()) == null) {
            return;
        }
        urlInputStream = urlConnection.getInputStream();
        spf = SAXParserFactory.newInstance();
        if (spf != null) {
            sp = spf.newSAXParser();
            sp.parse(urlInputStream, this);
        }
        if (urlInputStream != null) urlInputStream.close();
    }
}
