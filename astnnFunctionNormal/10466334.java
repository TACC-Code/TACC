class BackupThread extends Thread {
    public AChannel getChannel(Point p) {
        try {
            return getChannel(getPlotter().getInsideChannelIndex(p));
        } catch (Exception e) {
            return null;
        }
    }
}
