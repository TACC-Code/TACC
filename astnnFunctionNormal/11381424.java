class BackupThread extends Thread {
    protected int skipOffTokenChannels(int i) {
        int n = tokens.size();
        while (i < n && ((Token) tokens.get(i)).getChannel() != channel) {
            i++;
        }
        return i;
    }
}
