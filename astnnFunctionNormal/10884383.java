class BackupThread extends Thread {
    private boolean channelsOk() {
        log.info("Check channels...");
        if (parseTree == null) {
            setMessage("Expression not set up in parse tree. Use '" + CHECK + "'.");
            return false;
        }
        try {
            boolean[] expressionChannels = parseTree.getChannelList();
            if (expressionChannels.length > channelList.size()) {
                if (channelList.size() == 0) setMessage("Channels not set. Drag and Drop channels to channel list."); else setMessage("Missing some channel or channels needed in the expression");
                return false;
            }
        } catch (Throwable e) {
            setDetailFromException(e);
            setMessage("The parsed expression tree failed to return clannel list.\n" + "Click on 'Details' for details.\n");
            return false;
        }
        return true;
    }
}
