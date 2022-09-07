class BackupThread extends Thread {
    @Override
    public Object get(Selenium selenium, String locator) {
        setUrl(getUrl() + "?");
        for (ParameterMapper p : params) setUrl(getUrl() + (p.getId() + "=" + p.getValue(selenium) + "&"));
        HttpClient httpclient = new DefaultHttpClient();
        ByteBuffer bb = new ByteBuffer();
        try {
            HttpGet httpget = new HttpGet(getUrl());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                byte[] tmp = new byte[2048];
                while ((instream.read(tmp)) != -1) {
                    for (int i = 0; i < 2048; i++) bb.append(tmp[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bb;
    }
}
