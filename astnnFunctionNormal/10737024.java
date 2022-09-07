class BackupThread extends Thread {
    public void startResponse() {
        logger.debug("#doResponse.cid:" + getChannelId());
        HeaderParser requestHeader = getRequestHeader();
        if (!requestHeader.isWs()) {
            super.startResponse();
            return;
        }
        isWs = true;
        MappingResult mapping = getRequestMapping();
        RequestContext requestContext = getRequestContext();
        requestContext.registerLogoutEvnet(this);
        wsProtocol = WsProtocol.createWsProtocol(requestHeader, getRequestMapping());
        if (wsProtocol == null) {
            completeResponse("400");
            logger.warn("not found WebSocket Protocol");
            return;
        }
        logger.debug("wsProtocol class:" + wsProtocol.getClass().getName());
        String selectSubprotocol = null;
        String reqSubprotocols = wsProtocol.getRequestSubProtocols(requestHeader);
        if (reqSubprotocols == null) {
            if (wsProtocol.isUseSubprotocol()) {
                completeResponse("400");
                return;
            }
        } else {
            selectSubprotocol = wsProtocol.checkSubprotocol(reqSubprotocols);
            if (selectSubprotocol == null) {
                logger.debug("WsHybi10#suprotocol error.webSocketProtocol:" + reqSubprotocols);
                completeResponse("400");
                return;
            }
        }
        startWebSocketResponse(requestHeader, selectSubprotocol);
    }
}
