class BackupThread extends Thread {
    public Channel[] getChannels(short[] addresses) {
        Channel[] c = new Channel[addresses.length];
        for (int i = 0; i < addresses.length; i++) c[i] = getChannel(addresses[i]);
        return c;
    }
}
