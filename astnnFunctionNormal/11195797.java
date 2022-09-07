class BackupThread extends Thread {
    public void parse(String url) throws IOException, SAXException {
        try {
            parse(new InputSource((new URL(url)).openStream()));
        } catch (MalformedURLException ex) {
            throw new SAXException(ex);
        }
    }
}
