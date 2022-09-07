class BackupThread extends Thread {
    private SGSChannelData getChannelData(ClientSession ses) {
        String account = SGSBusinessProcess.accFromSesName(session.getName());
        SGSClientData data = getServer().getClientData(account);
        String channelName = data.get(SGSClientData.CURRENT_CHANNEL_NAME, String.class);
        return getServer().getChannelData(channelName);
    }
}
