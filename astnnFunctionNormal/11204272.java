class BackupThread extends Thread {
    public void readFile(URL url) throws PedroException, IOException, ParserConfigurationException, SAXException {
        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        read(inputStream);
    }
}
