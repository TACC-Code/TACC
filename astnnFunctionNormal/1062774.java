class BackupThread extends Thread {
    synchronized void startPull() {
        if (!waitingResponse.get()) {
            String url = "http://" + auth.domain + "/invoke/pull";
            final HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, url);
            request.setHeader("Host", auth.domain);
            request.setHeader(HttpHeaders.Names.CONNECTION, "keep-alive");
            if (null != C4ClientConfiguration.getInstance().getLocalProxy()) {
                ProxyInfo info = C4ClientConfiguration.getInstance().getLocalProxy();
                if (null != info.user) {
                    String userpass = info.user + ":" + info.passwd;
                    String encode = Base64.encodeToString(userpass.getBytes(), false);
                    request.setHeader(HttpHeaders.Names.PROXY_AUTHORIZATION, "Basic " + encode);
                }
            }
            request.setHeader(C4Constants.USER_TOKEN_HEADER, ConnectionHelper.getUserToken());
            request.setHeader("TransactionTime", C4ClientConfiguration.getInstance().getPullTransactionTime());
            request.setHeader(HttpHeaders.Names.USER_AGENT, C4ClientConfiguration.getInstance().getUserAgent());
            request.setHeader(HttpHeaders.Names.CONTENT_LENGTH, 0);
            ChannelFuture future = getRemoteFuture();
            if (future.getChannel().isConnected()) {
                future.getChannel().write(request);
                if (logger.isDebugEnabled()) {
                    logger.debug("Write pull request:" + request);
                }
            } else {
                future.addListener(new ChannelFutureListener() {

                    @Override
                    public void operationComplete(ChannelFuture f) throws Exception {
                        if (f.isSuccess()) {
                            f.getChannel().write(request);
                            if (logger.isDebugEnabled()) {
                                logger.debug("Write pull request:" + request);
                            }
                        }
                    }
                });
            }
            waitingResponse.set(true);
        }
    }
}
