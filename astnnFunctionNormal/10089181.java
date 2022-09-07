class BackupThread extends Thread {
    protected String sendHTTPPost(String urlString, String postData, boolean followRedirect) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setInstanceFollowRedirects(followRedirect);
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(postData);
        wr.flush();
        StringBuffer response = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
        }
        wr.close();
        rd.close();
        String responseString = response.toString();
        if (responseString.equals("") && !followRedirect) {
            responseString = con.getHeaderField("Location");
        }
        if (log.isDebugEnabled()) log.debug("Response string: " + responseString);
        return responseString;
    }
}
