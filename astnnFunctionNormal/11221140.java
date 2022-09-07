class BackupThread extends Thread {
    public void parseURL(URL url) throws SAXException, IOException {
        assert url != null;
        parseInputSource(new InputSource(url.openStream()));
    }
}
