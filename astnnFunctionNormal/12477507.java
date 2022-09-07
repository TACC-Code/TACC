class BackupThread extends Thread {
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
        return mClient.execute(target, request);
    }
}
