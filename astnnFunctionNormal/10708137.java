class BackupThread extends Thread {
    private void setupChannels() throws IOException, SAPIException {
        String[] channelList = controlPort.getChannels();
        String[] unitList = controlPort.getUnits();
        System.out.println("Preparing to listen to DAQ Channels: " + channelList[0]);
        for (int i = 1; i < channelList.length; i++) System.out.print(", " + channelList[i]);
        System.out.println(".");
        System.out.println("With corresponding units: " + unitList[0]);
        for (int i = 1; i < unitList.length; i++) System.out.print(", " + unitList[i]);
        System.out.println(".");
        if (unitList.length == channelList.length) postMetadata(channelList, unitList); else System.out.println("Warning, channel list and unit list are of " + "different lengths: units not posted!");
        for (int i = 0; i < channelList.length; i++) listener.registerChannel(channelList[i]);
    }
}
