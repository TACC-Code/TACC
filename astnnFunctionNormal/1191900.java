class BackupThread extends Thread {
    private InetSocketAddress executeTest(DatagramChannel channelToUse, InetSocketAddress testAddress, BlockingHook hook) {
        ICommFacade commFacade = connBroker.getCommFacade();
        IMessageProcessor msgProc = commFacade.getMessageProcessor();
        DatagramChannel dc = channelToUse;
        boolean unregisterAfterTest = commFacade.getChannelManager().getSelectionKey(channelToUse) == null;
        if (!unregisterAfterTest) commFacade.getChannelManager().registerChannel(dc);
        msgProc.installHook(hook, hook.getPredicate());
        try {
            try {
                InetSocketAddress result = null;
                commFacade.getChannelManager().registerChannel(dc);
                commFacade.sendUDPMessage(dc, new UDPPing(myPeerID), testAddress);
                IEnvelope env = hook.waitForMessage();
                if (env != null) result = ((MappedAddressResult) env.getMessage()).getMappedAddress();
                return result;
            } catch (IOException e) {
                Logger.logError(e, "Unable to send ping to test address!");
                return null;
            }
        } finally {
            msgProc.removeHook(hook);
            if (unregisterAfterTest) commFacade.getChannelManager().unregisterChannel(channelToUse);
        }
    }
}
