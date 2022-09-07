class BackupThread extends Thread {
    public boolean isConnected() {
        return xavgch.isConnected() && yavgch.isConnected() && bpmNode.getChannel(BPM.X_AVG_HANDLE).isConnected() && bpmNode.getChannel(BPM.Y_AVG_HANDLE).isConnected();
    }
}
