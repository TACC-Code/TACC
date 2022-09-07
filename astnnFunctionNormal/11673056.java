class BackupThread extends Thread {
    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
        return delegate.execute(request, context);
    }
}
