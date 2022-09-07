class BackupThread extends Thread {
    public void deleteWSRPPortlet(Portlet portlet) throws WSRPConsumerException {
        WSRPPortlets rootNode = getRootNode();
        if (rootNode == null) {
            return;
        }
        Iterator<WSRPPortlet> itr = rootNode.getWSRPPortlet().iterator();
        while (itr.hasNext()) {
            WSRPPortlet wsrpPortlet = itr.next();
            if (portlet.getPortletId().equals(wsrpPortlet.getChannelName())) {
                itr.remove();
                break;
            }
        }
        persistWSRPPortlets(rootNode);
    }
}
