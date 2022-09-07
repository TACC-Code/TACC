class BackupThread extends Thread {
    private void init() {
        log = new DebugLogger(managerName);
        this.connectionTypeName = "LeasePeer";
        this.setPort(23341);
        if (Global.serviceStatus != null) {
            ChannelUseDescriptionCollection cudc = Global.serviceStatus.getServiceDescription().getChannelUses();
            cudc.add(new ChannelUseDescription(LEASE_PEER_CONFIG_CHANNEL, "leases between message routing peers", 30L, 300L, new EventDescriptionCollection(new EventDescription(LEASE_PEER_CONFIG_NAMESPACE, "lease messages for access to messages from another peer")), new EventDescriptionCollection(new EventDescription(LEASE_PEER_CONFIG_NAMESPACE, "lease messages for access to messages from another peer"))));
        }
    }
}
