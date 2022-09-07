class BackupThread extends Thread {
    public void handle(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
        logger.info("Request: " + request.getRequestLine());
        Header hostHeader = request.getFirstHeader("Host");
        if (hostHeader == null || hostHeader.getValue() == null) {
            response.setStatusCode(400);
            return;
        }
        String[] tokens = hostHeader.getValue().split(":", 2);
        String host = tokens[0];
        int port = 80;
        try {
            port = Integer.parseInt(tokens[1]);
        } catch (Exception ex) {
        }
        if (remoteSocket != null) releaseSocket(remoteSocket);
        if ((remoteSocket = allocateSocket(host, port)) == null) {
            remoteSocket = new RemoteSocket(((HttpProxyConnectionFactory) factory).factory, host, port);
        }
        DefaultHttpClientConnection conn = new DefaultHttpClientConnection();
        conn.bind(remoteSocket, this.service.getParams());
        HttpRequestExecutor httpexecutor = new HttpRequestExecutor();
        HttpResponse remoteResponse = httpexecutor.execute(request, conn, context);
        StatusLine statusLine = remoteResponse.getStatusLine();
        response.setStatusLine(remoteResponse.getProtocolVersion(), statusLine.getStatusCode(), statusLine.getReasonPhrase());
        response.setHeaders(remoteResponse.getAllHeaders());
        response.setEntity(remoteResponse.getEntity());
        logger.info("Response: " + response.getStatusLine());
    }
}
