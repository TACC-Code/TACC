class BackupThread extends Thread {
    private void handleDeferredRegistrations() {
        synchronized (deferredRegistrations) {
            int deferredListSize = deferredRegistrations.size();
            for (int i = 0; i < deferredListSize; i++) {
                EventHandler eventHandler = (EventHandler) deferredRegistrations.get(i);
                if (orb.transportDebugFlag) {
                    dprint(".handleDeferredRegistrations: " + eventHandler);
                }
                SelectableChannel channel = eventHandler.getChannel();
                SelectionKey selectionKey = null;
                try {
                    selectionKey = channel.register(selector, eventHandler.getInterestOps(), (Object) eventHandler);
                } catch (ClosedChannelException e) {
                    if (orb.transportDebugFlag) {
                        dprint(".handleDeferredRegistrations: " + e);
                    }
                }
                eventHandler.setSelectionKey(selectionKey);
            }
            deferredRegistrations.clear();
        }
    }
}
