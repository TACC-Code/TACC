class BackupThread extends Thread {
    private void setupChannels() throws IOException, SAPIException {
        String[] channelList = controlPort.getChannels();
        String[] unitList = controlPort.getUnits();
        String[] ubList = controlPort.getUpperbounds();
        String[] lbList = controlPort.getLowerbounds();
        System.out.print("Prepairing to listen to DAQ Channels: ");
        System.out.print(channelList[0]);
        for (int i = 1; i < channelList.length; i++) System.out.print(", " + channelList[i]);
        System.out.println(".");
        System.out.print("With corresponding units: ");
        System.out.print(unitList[0]);
        for (int i = 1; i < unitList.length; i++) System.out.print(", " + unitList[i]);
        System.out.println(".");
        if (ubList.length > 0) {
            System.out.print("With corresponding upperbounds: ");
            System.out.print(ubList[0]);
            for (int i = 1; i < ubList.length; i++) System.out.print(", " + ubList[i]);
            System.out.println(".");
        }
        if (lbList.length > 0) {
            System.out.print("With corresponding lowerbounds: ");
            System.out.print(lbList[0]);
            for (int i = 1; i < lbList.length; i++) System.out.print(", " + lbList[i]);
            System.out.println(".");
        }
        if (unitList.length == channelList.length) postMetadata(channelList, unitList, ubList, lbList); else System.out.println("Warning, channel list and unit list are of " + "different lengths: units not posted!");
        for (int i = 0; i < channelList.length; i++) listener.registerChannel(channelList[i]);
    }
}
