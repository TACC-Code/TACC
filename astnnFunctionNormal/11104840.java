class BackupThread extends Thread {
    public boolean buildPConnsFromDB() {
        pcs.clear();
        List<ProcletPort> ports = ProcletModels.getInstance().getPorts();
        List items = DBConnection.getObjectsForClass("StoredPortConnection");
        for (Object o : items) {
            StoredPortConnection spc = (StoredPortConnection) o;
            String iportid = spc.getInput();
            String channel = spc.getChannel();
            String oportid = spc.getOutput();
            ProcletPort iPort = null;
            ProcletPort oPort = null;
            for (ProcletPort port : ports) {
                if (port.getPortID().equals(iportid)) {
                    iPort = port;
                }
                if (port.getPortID().equals(oportid)) {
                    oPort = port;
                }
            }
            PortConnection pconn = new PortConnection(iPort, oPort, channel);
            addPortConnection(pconn);
        }
        for (PortConnection pconn : pcs) {
            if (!channels.contains(pconn.getChannel())) {
                channels.add(pconn.getChannel());
            }
        }
        return true;
    }
}
