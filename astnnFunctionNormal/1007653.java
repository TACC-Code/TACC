class BackupThread extends Thread {
    public Channel[] getAllChannels() {
        Channel[] c = new Channel[maxChannel];
        for (short i = 0; i < maxChannel; i++) c[i] = getChannel((short) (i + 1));
        return c;
    }
}
