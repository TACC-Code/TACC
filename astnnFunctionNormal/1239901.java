class BackupThread extends Thread {
    public boolean isOutside(final int x, final int y) {
        int channel = getChannel(x, y);
        return channel > 512 || x < xOffset || x > xOffset + totalWidth || y < yOffset || y > yOffset + totalHeight;
    }
}
