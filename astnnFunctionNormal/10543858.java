class BackupThread extends Thread {
    public List parseConsoleChannelsPref(Object channelsPrefsValue) {
        if (channelsPrefsValue == null) return Collections.EMPTY_LIST;
        Map allChannels = getChannels();
        List channels = new LinkedList();
        int[] channelNumbers = (int[]) channelsPrefsValue;
        for (int i = 0; i < channelNumbers.length; i++) channels.add(allChannels.get(new Integer(channelNumbers[i])));
        return channels;
    }
}
