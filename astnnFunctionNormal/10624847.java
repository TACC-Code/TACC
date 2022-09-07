class BackupThread extends Thread {
    private void resetRF_Phases(Vector<DevTreeNode> rfCalcNodeV) {
        for (DevTreeNode rfNode : rfCalcNodeV) {
            if (rfNode.isOn == true) {
                RfCavity rfCav = (RfCavity) rfNode.accNode;
                try {
                    double livePhase = rfNode.hashT.get("livePhase").doubleValue();
                    rfCav.channelSuite().getChannel("cavPhaseSet").putVal(livePhase);
                } catch (ChannelException extp) {
                }
            }
        }
    }
}
