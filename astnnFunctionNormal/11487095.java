class BackupThread extends Thread {
    protected ProxyChannel getProxyChannel() {
        return (ProxyChannel) getChannel();
    }
}
