class BackupThread extends Thread {
    public HttpResponse execute(HttpHost host, HttpRequest req) throws IOException, ClientProtocolException {
        return execute(host, req, (HttpContext) null);
    }
}
