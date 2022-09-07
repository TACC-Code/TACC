class BackupThread extends Thread {
    public int getChannel(final int x, final int y) {
        int xx = x - xOffset;
        int yy = y - yOffset;
        int channel = (yy / boxHeight) * COLUMN_COUNT + (xx / boxWidth) + 1;
        return channel;
    }
}
