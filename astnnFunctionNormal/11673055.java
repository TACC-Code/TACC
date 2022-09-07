class BackupThread extends Thread {
    public HttpResponse execute(HttpUriRequest request) throws IOException {
        return delegate.execute(request);
    }
}
