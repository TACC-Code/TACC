class BackupThread extends Thread {
    public static String sendSynchronousHttpGetRequest(String url) throws MalformedURLException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder result = new StringBuilder();
        try {
            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                result.append(inputLine);
            }
        } catch (Exception e) {
            log.warning(e.getMessage());
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                log.warning(e.getMessage());
            }
        }
        return result.toString();
    }
}
