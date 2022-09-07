class BackupThread extends Thread {
    public void setLayout(URL url) throws SAXException, IOException {
        setLayout(url.openStream());
    }
}
