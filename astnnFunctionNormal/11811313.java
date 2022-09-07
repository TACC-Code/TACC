class BackupThread extends Thread {
    public static String query(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        String result = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            result = firstLineFromStream(connection.getInputStream());
        } finally {
            connection.disconnect();
        }
        return result;
    }
}
