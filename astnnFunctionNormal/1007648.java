class BackupThread extends Thread {
    public short[] getChannelValues(short[] addresses) {
        short[] values = new short[addresses.length];
        for (int i = 0; i < addresses.length; i++) values[i] = getChannelValue(addresses[i]);
        return values;
    }
}
