class BackupThread extends Thread {
    public static byte[] query(String server_query_prefix, String overlayID, byte[] input_message) throws MalformedURLException, IOException {
        String urlString = server_query_prefix + "?cmd=msg&OverlayID=" + encodeURLString(overlayID) + "&msg=" + toHexString(input_message);
        URL url = new URL(urlString);
        String result = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            result = firstLineFromStream(connection.getInputStream());
        } finally {
            connection.disconnect();
        }
        return toByteArray(result);
    }
}
