class BackupThread extends Thread {
    protected void updatePortlet(WSRPPortlets wsrpPortlets, WSRPPortlet wsrpPortlet) throws WSRPConsumerException {
        if (wsrpPortlets == null) {
            return;
        }
        Iterator<WSRPPortlet> itr = wsrpPortlets.getWSRPPortlet().iterator();
        while (itr.hasNext()) {
            WSRPPortlet curWSRPPortlet = itr.next();
            if (curWSRPPortlet.getChannelName().equals(wsrpPortlet.getChannelName())) {
                itr.remove();
                break;
            }
        }
        wsrpPortlets.getWSRPPortlet().add(wsrpPortlet);
        persistWSRPPortlets(wsrpPortlets);
    }
}
