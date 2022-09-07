class BackupThread extends Thread {
    public static InputStream getInputStreamOfURL(String urlString) throws IOException {
        InputStream inputStream;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
        } catch (IOException e) {
            inputStream = URLLocalFileCache.getInputStream(urlString);
            if (inputStream == null) {
                throw e;
            }
        }
        return inputStream;
    }
}
