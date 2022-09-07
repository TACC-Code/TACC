class BackupThread extends Thread {
    protected void fillBuffer() {
        int index = 0;
        Token t = tokenSource.nextToken();
        while (t != null && t.getType() != CharStream.EOF) {
            boolean discard = false;
            if (channelOverrideMap != null) {
                Integer channelI = (Integer) channelOverrideMap.get(new Integer(t.getType()));
                if (channelI != null) {
                    t.setChannel(channelI.intValue());
                }
            }
            if (discardSet != null && discardSet.contains(new Integer(t.getType()))) {
                discard = true;
            } else if (discardOffChannelTokens && t.getChannel() != this.channel) {
                discard = true;
            }
            if (!discard) {
                t.setTokenIndex(index);
                tokens.add(t);
                index++;
            }
            t = tokenSource.nextToken();
        }
        p = 0;
        p = skipOffTokenChannels(p);
    }
}
