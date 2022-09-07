class BackupThread extends Thread {
    private static String sendRequest(String requestXml, String hostAddress) throws GarantiApiException {
        String responseXml = null;
        OutputStream os = null;
        BufferedReader reader = null;
        StringBuilder stringBuilder = null;
        try {
            String data = "data=" + requestXml;
            byte[] byteArray = data.getBytes("UTF8");
            URL url = new URL(hostAddress);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", new Integer(byteArray.length).toString());
            conn.setDoOutput(true);
            conn.connect();
            os = conn.getOutputStream();
            os.write(byteArray);
            os.close();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            responseXml = stringBuilder.toString();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new GarantiApiException("Error Occured During Posting XmlData.", e.getCause());
        }
        return responseXml;
    }
}
