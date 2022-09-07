class BackupThread extends Thread {
    private static void downloadFile(String filePath, File destination) throws IOException, MalformedURLException {
        URLConnection connection = null;
        InputStream is = null;
        FileOutputStream destinationFile = null;
        URL url = new URL(filePath);
        connection = url.openConnection();
        destinationFile = new FileOutputStream(destination);
        is = new BufferedInputStream(connection.getInputStream());
        int currentBit = 0;
        while ((currentBit = is.read()) != -1) {
            destinationFile.write(currentBit);
        }
        is.close();
        destinationFile.flush();
        destinationFile.close();
    }
}
