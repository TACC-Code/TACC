class BackupThread extends Thread {
    protected int skipOffTokenChannelsReverse(int i) {
        while (i >= 0 && ((Token) tokens.get(i)).getChannel() != channel) {
            i--;
        }
        return i;
    }
}
