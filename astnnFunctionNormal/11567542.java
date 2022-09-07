class BackupThread extends Thread {
    protected HttpResponse execute(HttpRequestBase request) throws IOException {
        try {
            this.context.removeAttribute(DefaultRedirectStrategy.REDIRECT_LOCATIONS);
            return this.client.execute(request, this.context);
        } catch (IOException e) {
            request.abort();
            throw e;
        }
    }
}
