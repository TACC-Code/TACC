class BackupThread extends Thread {
    private static int getElementSize(final ByteBuffer format) {
        final int channelOrder = format.getInt(format.position() + 0);
        final int channelType = format.getInt(format.position() + 4);
        return getChannelCount(channelOrder) * getChannelSize(channelType);
    }
}
