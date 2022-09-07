class BackupThread extends Thread {
    public void readFile(URL zipFile) throws PedroException, IOException, ParserConfigurationException, SAXException {
        URLConnection urlConnection = zipFile.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        String zipFileName = zipFile.getFile();
        readFile(zipFileName, inputStream);
    }
}
