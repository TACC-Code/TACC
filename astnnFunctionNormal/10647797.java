class BackupThread extends Thread {
    public static void setupAdvertising() {
        String msg = Common.buildEventString(false);
        SimulHandler.getHandler().qaddevent(msg);
        if (SimulHandler.getChannelOut()) {
            Advertiser.start();
        } else {
            Advertiser.stop();
        }
    }
}
