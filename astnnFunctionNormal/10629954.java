class BackupThread extends Thread {
    protected void updateChannelsInspector() {
        LoggerSessionHandler handler = _model.getSelectedSessionHandler();
        if (handler != null) {
            Vector connectedPVs = new Vector();
            Vector unconnectedPVs = new Vector();
            Collection channelRefs = handler.getChannelRefs();
            for (Iterator iter = channelRefs.iterator(); iter.hasNext(); ) {
                ChannelRef channelRef = (ChannelRef) iter.next();
                if (channelRef.isConnected()) {
                    connectedPVs.add(channelRef);
                } else {
                    unconnectedPVs.add(channelRef);
                }
            }
            Collections.sort(connectedPVs);
            Collections.sort(unconnectedPVs);
            _connectedPVList.setListData(connectedPVs);
            _unconnectedPVList.setListData(unconnectedPVs);
        } else {
            _connectedPVList.setListData(new Vector());
            _unconnectedPVList.setListData(new Vector());
        }
    }
}
