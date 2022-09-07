class BackupThread extends Thread {
    public static boolean isKeyInCache(String channelName) {
        return getChannelCache().isKeyInCache(channelName);
    }
}
