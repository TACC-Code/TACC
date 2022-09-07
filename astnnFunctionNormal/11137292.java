class BackupThread extends Thread {
    public void write(NetworkRequest networkRequest) {
        try {
            networkRequest.getChannel().close();
        } catch (IOException e) {
            log.info("Error closing channel", e);
        }
    }
}
