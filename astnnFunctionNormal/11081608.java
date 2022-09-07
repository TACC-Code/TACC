class BackupThread extends Thread {
    public String getChannelName() {
        return "JETTY_HTTPSESSION_DISTRIBUTION:" + getContextPath() + "-" + getSubClusterName();
    }
}
