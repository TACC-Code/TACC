class BackupThread extends Thread {
    boolean contains_channel(Collection channelsInfo, TVChannel chan) {
        Iterator itCh = channelsInfo.iterator();
        while (itCh.hasNext()) {
            TVChannelsSet.Channel chinfo = (TVChannelsSet.Channel) itCh.next();
            if (chinfo.getChannelID().equals(chan.getID())) {
                return true;
            }
        }
        return false;
    }
}
