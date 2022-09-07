class BackupThread extends Thread {
    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://www.google.com");
        httpget.setHeader("User-Agent", "MySuperUserAgent");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            int l;
            byte[] tmp = new byte[2048];
            while ((l = instream.read()) != -1) {
                StringBuilder builder = new StringBuilder(l);
                builder.append((char) l);
                System.out.print(builder);
            }
        }
    }
}
