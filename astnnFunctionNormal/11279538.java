class BackupThread extends Thread {
    private void completeProxyRequest(Buffer buffer) {
        int length = proxyEvent.getContentLength();
        int rest = length - proxyEvent.content.readableBytes();
        proxyEvent.content.write(buffer, rest);
        if (isProxyRequestReady(proxyEvent)) {
            getClientConnection(proxyEvent).send(proxyEvent);
            status = ProxySessionStatus.WAITING_NORMAL_RESPONSE;
        }
        if (buffer.readable() && buffer != uploadBuffer) {
            uploadBuffer.write(buffer, buffer.readableBytes());
        }
        if (proxyEvent.containsHeader(HttpHeaders.Names.CONTENT_RANGE)) {
            status = ProxySessionStatus.WATING_RANGE_UPLOAD_RESPONSE;
        }
    }
}
