class BackupThread extends Thread {
    private ChannelTree getChannelTree() {
        try {
            sMap = new ChannelMap();
            sink.RequestRegistration();
            sMap = sink.Fetch(-1, sMap);
            return ChannelTree.createFromChannelMap(sMap);
        } catch (SAPIException se) {
            System.out.println("ChannelList: get channel tree failed. Reconnect?");
            System.out.println("Exception = " + se);
            connected = false;
        }
        return null;
    }
}
