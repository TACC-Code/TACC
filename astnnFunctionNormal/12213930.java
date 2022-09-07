class BackupThread extends Thread {
    private static void buildChannels(final Show show, final int numberOfChannels) {
        for (int i = 0; i < numberOfChannels; i++) {
            Channel channel = new Channel(show.getDirty(), i, "Channel " + (i + 1));
            show.getChannels().add(channel);
        }
    }
}
