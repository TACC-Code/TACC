class BackupThread extends Thread {
    public void updateConfig() {
        String[] channels = wikimediaBot.getChannels();
        String channelStr = "";
        for (int i = 0; i < channels.length; i++) if (i == 0) channelStr += channels[i]; else channelStr += ", " + channels[i];
        config.setProperty("channel", channelStr);
        String[] vchannels = wikimediaBot.getChannels();
        String vchannelStr = "";
        for (int i = 0; i < vchannels.length; i++) if (i == 0) vchannelStr += vchannels[i]; else vchannelStr += ", " + vchannels[i];
        config.setProperty("vandalismchannel", vchannelStr);
    }
}
