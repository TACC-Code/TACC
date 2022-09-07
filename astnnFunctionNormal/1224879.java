class BackupThread extends Thread {
    public static boolean nodeCanConnect(BPM bpm) {
        boolean canConnectx = true;
        boolean canConnecty = true;
        boolean canConnect = false;
        try {
            canConnectx = bpm.getChannel(BPM.X_TBT_HANDLE).connectAndWait();
            canConnectx = bpm.getChannel(BPM.Y_TBT_HANDLE).connectAndWait();
        } catch (NoSuchChannelException excpt) {
            if (!canConnectx || !canConnecty) {
                canConnect = false;
            }
        }
        return canConnect;
    }
}
