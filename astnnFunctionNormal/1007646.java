class BackupThread extends Thread {
    public short getChannelValue(short address) {
        Channel c = channels.get(address);
        if (c != null) {
            if (c.value >= 0) return c.value; else return -100;
        } else return 0;
    }
}
