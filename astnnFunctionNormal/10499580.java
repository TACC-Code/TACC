class BackupThread extends Thread {
    public boolean doReplay(ProxyHandler handler, ByteBuffer[] body) {
        logger.debug("#doReplay cid:" + handler.getChannelId());
        AccessLog accessLog = handler.getAccessLog();
        HeaderParser requestHeader = handler.getRequestHeader();
        MappingResult mapping = handler.getRequestMapping();
        AccessLog recodeLog = searchAccessLog(accessLog, requestHeader, mapping);
        if (recodeLog != null) {
            accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_REPLAY);
            handler.setStatusCode(recodeLog.getStatusCode());
            String contentType = recodeLog.getContentType();
            if (contentType != null) {
                handler.setContentType(contentType);
            }
        }
        File file = searchFile(requestHeader, mapping);
        if (file != null) {
            accessLog.setDestinationType(AccessLog.DESTINATION_TYPE_REPLAY);
            logger.debug("response from file.file:" + file.getAbsolutePath());
            handler.setRequestAttribute(ProxyHandler.ATTRIBUTE_RESPONSE_FILE, file);
            WebServerHandler response = (WebServerHandler) handler.forwardHandler(Mapping.FILE_SYSTEM_HANDLER);
            PoolManager.poolBufferInstance(body);
            return true;
        } else if (recodeLog != null) {
            String bodyDigest = recodeLog.getResponseBodyDigest();
            Store store = Store.open(bodyDigest);
            if (store != null) {
                logger.debug("response from trace.bodyDigest:" + bodyDigest);
                String contentEncoding = recodeLog.getContentEncoding();
                if (contentEncoding != null) {
                    handler.setHeader(HeaderParser.CONTENT_ENCODING_HEADER, contentEncoding);
                }
                String transferEncoding = recodeLog.getTransferEncoding();
                if (transferEncoding != null) {
                    handler.setHeader(HeaderParser.TRANSFER_ENCODING_HEADER, transferEncoding);
                } else {
                    long contentLength = recodeLog.getResponseLength();
                    handler.setContentLength(contentLength);
                }
                handler.setAttribute("Store", store);
                handler.forwardHandler(Mapping.STORE_HANDLER);
                PoolManager.poolBufferInstance(body);
                return true;
            }
        }
        return false;
    }
}
