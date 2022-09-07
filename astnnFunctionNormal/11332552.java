class BackupThread extends Thread {
    public void parse(String sourceName, URL url) throws SAXException, ParserConfigurationException, IOException {
        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
        parse(sourceName, uc.getInputStream());
    }
}
