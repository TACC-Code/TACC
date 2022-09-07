class BackupThread extends Thread {
    private void handleException(Exception e) {
        requestGlobals.store(e);
        try {
            errorServicer.service();
        } catch (Exception re) {
            log.error("Error while attempting to handle exception, closing connection", re);
            try {
                networkRequest.getChannel().close();
            } catch (IOException ioe) {
                log.info("Exception while closing SocketChannel", ioe);
            }
        }
    }
}
