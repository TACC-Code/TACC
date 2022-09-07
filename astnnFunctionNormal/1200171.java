class BackupThread extends Thread {
    public List getFreeChannels(GMChannelRoute forRoute) {
        List freeChannels = new ArrayList();
        for (int ch = 0; ch < MAX_CHANNELS; ch++) {
            if (ch != PERCUSSION_CHANNEL) {
                boolean isFreeChannel = true;
                Iterator channelIt = this.midiChannels.iterator();
                while (channelIt.hasNext()) {
                    GMChannelRoute route = (GMChannelRoute) channelIt.next();
                    if (forRoute == null || !forRoute.equals(route)) {
                        if (route.getChannel1() == ch || route.getChannel2() == ch) {
                            isFreeChannel = false;
                        }
                    }
                }
                if (isFreeChannel) {
                    freeChannels.add(new Integer(ch));
                }
            }
        }
        return freeChannels;
    }
}
