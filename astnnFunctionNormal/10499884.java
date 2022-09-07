class BackupThread extends Thread {
    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        localChannel = e.getChannel();
        if (null == eventService) {
            e.getChannel().write(new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_IMPLEMENTED)).addListener(ChannelFutureListener.CLOSE);
            return;
        }
        if (!isReadingChunks) {
            if (!ishttps) {
                if (e.getMessage() instanceof HttpRequest) {
                    HttpRequest request = (HttpRequest) e.getMessage();
                    if (request.getMethod().equals(HttpMethod.CONNECT)) {
                        ishttps = true;
                    }
                    if (request.isChunked()) {
                        isReadingChunks = true;
                    }
                    HttpProxyEventType type = ishttps ? HttpProxyEventType.RECV_HTTPS_REQUEST : HttpProxyEventType.RECV_HTTP_REQUEST;
                    HttpProxyEvent event = new HttpProxyEvent(type, request, e.getChannel());
                    eventService.handleEvent(event, this);
                } else {
                    HttpProxyEvent event = new HttpProxyEvent(HttpProxyEventType.RECV_HTTP_CHUNK, e.getMessage(), e.getChannel());
                    eventService.handleEvent(event, this);
                }
            } else {
                if (e.getMessage() instanceof HttpRequest) {
                    HttpProxyEvent event = new HttpProxyEvent(HttpProxyEventType.RECV_HTTPS_REQUEST, e.getMessage(), e.getChannel());
                    eventService.handleEvent(event, this);
                } else {
                    HttpProxyEvent event = new HttpProxyEvent(HttpProxyEventType.RECV_HTTPS_CHUNK, e.getMessage(), e.getChannel());
                    eventService.handleEvent(event, this);
                }
            }
        } else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
            if (chunk.isLast()) {
                isReadingChunks = false;
            }
            HttpProxyEvent event = new HttpProxyEvent(HttpProxyEventType.RECV_HTTP_CHUNK, chunk, e.getChannel());
            eventService.handleEvent(event, this);
        }
    }
}
