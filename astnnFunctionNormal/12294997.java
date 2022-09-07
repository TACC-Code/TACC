class BackupThread extends Thread {
    public static JSONObject makeRequest(String url, String query) throws URISyntaxException, ClientProtocolException, IOException, JSONException {
        URI u = new URI("http://services.digg.com/" + url + "?type=json&appkey=" + APP_ID + query);
        Log.v(TAG, "Reqesting URI: " + u);
        HttpClient client = new DefaultHttpClient();
        HttpGet r = new org.apache.http.client.methods.HttpGet(u);
        r.setHeader("User-Agent", userAgent);
        HttpResponse p = client.execute(r);
        InputStream reader = p.getEntity().getContent();
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = reader.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return new JSONObject(new JSONTokener(out.toString()));
    }
}
