class BackupThread extends Thread {
    private HttpResponse execute(HttpRequestBase requestBase) throws IOException, ClientProtocolException, ProcessException {
        HttpResponse res = client.execute(requestBase);
        StatusLine statusLine = res.getStatusLine();
        int code = statusLine.getStatusCode();
        if (code >= HttpStatus.SC_BAD_REQUEST) {
            throw new ProcessException("invalid status: " + statusLine + "; for " + requestBase.getURI());
        }
        return res;
    }
}
