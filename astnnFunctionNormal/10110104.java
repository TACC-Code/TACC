class BackupThread extends Thread {
    public boolean isChannelToken(String token) {
        ServerInformation serverInfo = getServerInformation();
        String[] chanPrefixes = serverInfo.getChannelPrefixes();
        for (String prefix : chanPrefixes) {
            if (token.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
