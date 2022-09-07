class BackupThread extends Thread {
    public ChannelVO insertChannel(String url) {
        return getChannelDAO().insert(new ChannelVO(url, url));
    }
}
