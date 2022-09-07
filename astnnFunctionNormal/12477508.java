class BackupThread extends Thread {
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
        return mClient.execute(target, request, context);
    }
}
