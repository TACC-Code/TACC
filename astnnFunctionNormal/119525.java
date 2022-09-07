class BackupThread extends Thread {
    private void sortByNum() {
        int i, j, w;
        for (i = count - 1; i >= 0; i--) {
            ChannelItem ch = chans[i];
            w = ch.getUsers();
            j = i;
            while ((j < count - 1) && (chans[j + 1].getUsers() > w)) {
                chans[j] = chans[j + 1];
                j++;
            }
            chans[j] = ch;
        }
    }
}
