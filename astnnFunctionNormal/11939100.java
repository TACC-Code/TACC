class BackupThread extends Thread {
    protected String doPostString(String url, NameValuePair data[], String referer) throws Exception {
        getHttpClient().getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        if (url.indexOf("?") != -1) url = url + "&dd=" + System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        setHeaders(post, referer);
        post.addHeader("refer", referer);
        post.addHeader("Host", "login.dbank.com");
        post.setHeader("Content-Type", "text/html; charset=" + ConstantParam.ENCODING);
        post.setEntity(new UrlEncodedFormEntity(data, ConstantParam.ENCODING));
        HttpProtocolParams.setUseExpectContinue(getHttpClient().getParams(), false);
        HttpProtocolParams.setUseExpectContinue(post.getParams(), false);
        HttpResponse resp = getHttpClient().execute(post, getHttpClient().getDefaultContext());
        InputStream content = resp.getEntity().getContent();
        return readInputStream(content, ConstantParam.ENCODING);
    }
}
