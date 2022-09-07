class BackupThread extends Thread {
    public static String getURL(String URLToGet) throws HTTPNotOKException {
        String pageContents = "";
        URL url = null;
        HttpURLConnection connection = null;
        int responseCode = 0;
        try {
            url = new URL(URLToGet);
            connection = (HttpURLConnection) url.openConnection();
            responseCode = connection.getResponseCode();
        } catch (MalformedURLException ex) {
            System.err.println("Error: Malformed URL");
        } catch (IOException ex) {
            System.err.println("Error: IO Exception");
        }
        if (responseCode != 200) {
            throw new HTTPNotOKException(responseCode);
        } else {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    pageContents += inputLine;
                }
            } catch (IOException ex) {
                System.err.println("Error: IO Exception");
            }
        }
        return pageContents;
    }
}
