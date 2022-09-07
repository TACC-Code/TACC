class BackupThread extends Thread {
    public void initialize(NetworkInterface iface) {
        Category log = ThreadCategory.getInstance(getClass());
        if (iface.getType() != NetworkInterface.TYPE_IPV4) throw new RuntimeException("Unsupported interface type, only TYPE_IPV4 currently supported");
        InetAddress ipAddr = (InetAddress) iface.getAddress();
        SnmpPeer peer = SnmpPeerFactory.getInstance().getPeer(ipAddr, SnmpSMI.SNMPV1);
        if (log.isDebugEnabled()) {
            String nl = System.getProperty("line.separator");
            log.debug("initialize: SnmpPeer configuration: address: " + peer.getPeer() + nl + "      version: " + ((peer.getParameters().getVersion() == SnmpSMI.SNMPV1) ? "SNMPv1" : "SNMPv2") + nl + "      timeout: " + peer.getTimeout() + nl + "      retries: " + peer.getRetries() + nl + "      read commString: " + peer.getParameters().getReadCommunity() + nl + "      write commString: " + peer.getParameters().getWriteCommunity());
        }
        if (log.isDebugEnabled()) log.debug("initialize: setting SNMP peer attribute for interface " + ipAddr.getHostAddress());
        iface.setAttribute(SNMP_PEER_KEY, peer);
        log.debug("initialize: interface: " + ipAddr.getHostAddress() + " initialized.");
        return;
    }
}
