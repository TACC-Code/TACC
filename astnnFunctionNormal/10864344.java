class BackupThread extends Thread {
    protected void fillBuffer(int k) {
        int no = 0;
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
                t.setTokenIndex(tokenIndex);
                tokens.add(t);
                tokenIndex++;
                if (t.getChannel() == channel) {
                    if (++no == k) {
                        p = skipOffTokenChannels(p);
                        break;
                    }
                }
            }
            t = tokenSource.nextToken();
        }
    }
}
