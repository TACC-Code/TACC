class BackupThread extends Thread {
    public ChannelModel getChannelModelWithPV(String pvName) {
        ChannelModel channelModel = null;
        for (int index = 0; index < channelModels.length; index++) {
            if (channelModels[index].getChannel() != null) {
                if (channelModels[index].getChannel().channelName().equals(pvName)) {
                    return channelModels[index];
                }
            }
        }
        return channelModel;
    }
}
