class BackupThread extends Thread {
    public void sort() {
        Collections.sort(changes, new Comparator<ChannelChange>() {

            public int compare(final ChannelChange cc1, final ChannelChange cc2) {
                return cc1.getChannelId() - cc2.getChannelId();
            }
        });
    }
}
