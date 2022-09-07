class BackupThread extends Thread {
    public HttpResponse execute(HttpUriRequest req, HttpContext ctx) throws IOException, ClientProtocolException {
        return execute(getHttpHost(req), req, ctx);
    }
}
