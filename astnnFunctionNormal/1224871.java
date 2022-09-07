class BackupThread extends Thread {
    public BpmAgent(AcceleratorSeq aSequence, BPM newBpmNode) {
        bpmNode = newBpmNode;
        sequence = aSequence;
        xavgch = bpmNode.getChannel(BPM.X_TBT_HANDLE);
        yavgch = bpmNode.getChannel(BPM.Y_TBT_HANDLE);
        xavgch.requestConnection();
        yavgch.requestConnection();
        bpmNode.getChannel(BPM.X_AVG_HANDLE).requestConnection();
        bpmNode.getChannel(BPM.Y_AVG_HANDLE).requestConnection();
    }
}
