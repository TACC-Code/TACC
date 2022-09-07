class BackupThread extends Thread {
    private List<URI> executeQuery(URI pdURI, String qs) throws IOException {
        URL url = new URL("http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + qs + "&key=" + GOOGLE_API_KEY);
        URLConnection connection = url.openConnection();
        connection.addRequestProperty("Referer", GOOGLE_QUERY_REFERER);
        String line;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        try {
            JSONObject json = new JSONObject(builder.toString());
            System.out.println("GOT: " + json.toString(2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
