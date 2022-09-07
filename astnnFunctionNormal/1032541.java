class BackupThread extends Thread {
    public HttpResponse execute(HttpHost host, HttpRequest req, HttpContext ctx) throws IOException, ClientProtocolException {
        try {
            return backend.execute(host, req, ctx);
        } catch (UnsuccessfulResponseException ure) {
            return ure.getResponse();
        } catch (CircuitBreakerException cbe) {
            throw new IOException(cbe);
        }
    }
}
