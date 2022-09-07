class BackupThread extends Thread {
    private HttpResponse executeRequest(HttpRequestBase request) {
        try {
            org.apache.http.HttpResponse rsp = client.execute(request);
            if (LOG.isTraceEnabled()) {
                LOG.trace(String.format("%s %s %s %s", request.getMethod(), request.getURI(), rsp.getStatusLine().getStatusCode(), rsp.getStatusLine().getReasonPhrase()));
            }
            return StdHttpResponse.of(rsp, request.getURI().toString());
        } catch (Exception e) {
            throw Exceptions.propagate(e);
        }
    }
}
