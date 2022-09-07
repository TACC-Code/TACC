class BackupThread extends Thread {
    public static void main(String args[]) throws IOException {
        URL url;
        URLConnection urlConnection;
        DataOutputStream outStream;
        String body = "query=" + URLEncoder.encode("elmo\ttoys\tgames", "UTF-8") + "&seeds=" + URLEncoder.encode("games 10\ttoys 100\t", "UTF-8") + "&n=100";
        url = new URL("http://130.89.13.204:8080/rankService/RankServlet?" + body);
        urlConnection = url.openConnection();
        ((HttpURLConnection) urlConnection).setRequestMethod("POST");
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
            System.out.println(buffer);
        }
    }
}
