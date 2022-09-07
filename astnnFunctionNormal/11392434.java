class BackupThread extends Thread {
    public static LinkedList<String[]> getSuggestions(HashSet<String> query, Hashtable<String, Float> seeds, int n) throws IOException {
        LinkedList<String[]> list = new LinkedList<String[]>();
        URL url;
        URLConnection urlConnection;
        DataOutputStream outStream;
        String query_text = setToString(query);
        String seeds_text = hashToString(seeds);
        String body = "query=" + URLEncoder.encode(query_text, "UTF-8") + "&seeds=" + URLEncoder.encode(seeds_text, "UTF-8") + "&n=" + n;
        url = new URL("http://130.89.13.204:8080/rankService/RankServlet?" + body);
        urlConnection = url.openConnection();
        ((HttpURLConnection) urlConnection).setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Content-Length", "" + body.length());
        outStream = new DataOutputStream(urlConnection.getOutputStream());
        InputStreamReader rea = new InputStreamReader(urlConnection.getInputStream());
        BufferedReader d = new BufferedReader(rea);
        outStream.writeBytes(body);
        outStream.flush();
        outStream.close();
        String buffer;
        while ((buffer = d.readLine()) != null) {
            String temp[] = buffer.split("\t");
            list.add(temp);
        }
        return list;
    }
}
