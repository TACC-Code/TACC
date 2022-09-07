class BackupThread extends Thread {
    public double getChannel(final double energy) {
        double channel = 0;
        final double bestDiff = Math.abs(getValue(channel) - energy);
        double diff;
        for (int i = 0; i < sizeHistogram; i++) {
            diff = Math.abs(getValue(i) - energy);
            if (diff < bestDiff) {
                channel = i;
            }
        }
        return channel;
    }
}
