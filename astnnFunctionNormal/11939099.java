class BackupThread extends Thread {
    protected InputStream doPost(HttpClient client, String url, NameValuePair data[], String referer) throws HttpException, IOException, InterruptedException, URISyntaxException {
        client.getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        if (url.indexOf("?") != -1) url = url + "&dd=" + System.currentTimeMillis();
        HttpPost post = new HttpPost(url);
        setHeaders(post, referer);
        post.setHeader("Content-Type", "text/html; charset=" + ConstantParam.ENCODING);
        post.setEntity(new UrlEncodedFormEntity(data, ConstantParam.ENCODING));
        HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
        HttpProtocolParams.setUseExpectContinue(post.getParams(), false);
        HttpResponse resp = client.execute(post, client.getDefaultContext());
        InputStream content = resp.getEntity().getContent();
        return content;
    }
}
