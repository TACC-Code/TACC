class BackupThread extends Thread {
    protected InputStream doGet(HttpClient client, String url, String referer) throws URISyntaxException, InterruptedException, HttpException, IOException {
        getHttpClient().getConnectionManager().closeIdleConnections(0L, TimeUnit.MILLISECONDS);
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "text/html; charset=" + ConstantParam.ENCODING);
        setHeaders(get, referer);
        HttpResponse resp = getHttpClient().execute(get, getHttpClient().getDefaultContext());
        InputStream content = resp.getEntity().getContent();
        return content;
    }
}
