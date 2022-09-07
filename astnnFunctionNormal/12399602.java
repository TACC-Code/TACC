class BackupThread extends Thread {
    public boolean connect() {
        if (connected) return true;
        if (usesProvidedConnection) {
            jabberConnection = providedXMPPConnection;
            String fullUserName = jabberConnection.getUser();
            userName = fullUserName.substring(0, fullUserName.indexOf("@"));
            host = jabberConnection.getHost();
            port = jabberConnection.getPort();
            serviceName = jabberConnection.getServiceName();
            resource = fullUserName.substring(fullUserName.lastIndexOf("/") + 1);
            resourceSuffix = "";
        } else {
            XMPPConnection.addConnectionCreationListener(new ConnectionCreationListener() {

                @Override
                public void connectionCreated(Connection connection) {
                    new ServiceDiscoveryManager(connection);
                }
            });
            ConnectionConfiguration config = new ConnectionConfiguration(host, port);
            jabberConnection = new XMPPConnection(config);
            try {
                jabberConnection.connect();
                jabberConnection.login(userName, password, resource + resourceSuffix);
            } catch (XMPPException e) {
                e.printStackTrace();
                return false;
            }
        }
        roster = jabberConnection.getRoster();
        System.out.println(getJID() + " connected");
        Presence presence = new Presence(Presence.Type.available);
        jabberConnection.sendPacket(presence);
        packetProcessor = new PacketProcessor();
        packetProcessor.start();
        PacketFilter filter = new PacketTypeFilter(RPCPacket.class);
        PacketListener listener = new PacketListener() {

            public void processPacket(Packet packet) {
                if (packet == null) return;
                System.out.println("Received RPC IQ from: " + userName + ", at time: " + System.currentTimeMillis());
                XMPPError error = packet.getError();
                if (error != null) {
                    System.out.println("Received error message: " + error.toString());
                    System.out.println(packet.toXML());
                }
                IQ iqp = (IQ) packet;
                processIQPacket(iqp);
            }
        };
        jabberConnection.addPacketListener(listener, filter);
        connected = true;
        return true;
    }
}
