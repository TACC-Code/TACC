class BackupThread extends Thread {
    public boolean messengerReady(MessengerEvent event) {
        Messenger messenger = event.getMessenger();
        Messenger messengerForHere;
        EndpointAddress connAddr = event.getConnectionAddress();
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("New " + messenger + " for : " + messenger.getDestinationAddress() + " (" + messenger.getLogicalDestinationAddress() + ")");
        }
        int highestPrec = EndpointService.HighPrecedence;
        int lowestPrec = EndpointService.LowPrecedence;
        if (connAddr != null) {
            String cgServiceName = connAddr.getServiceName();
            if (cgServiceName == null || !cgServiceName.startsWith(ChannelMessenger.InsertedServicePrefix)) {
            } else if (!myServiceName.equals(cgServiceName)) {
                highestPrec = EndpointService.LowPrecedence;
            } else {
                lowestPrec = EndpointService.LowPrecedence + 1;
                String serviceParam = connAddr.getServiceParameter();
                String realService = null;
                String realParam = null;
                if (null != serviceParam) {
                    int slashAt = serviceParam.indexOf('/');
                    if (-1 == slashAt) {
                        realService = serviceParam;
                    } else {
                        realService = serviceParam.substring(0, slashAt);
                        realParam = serviceParam.substring(slashAt + 1);
                    }
                }
                connAddr = new EndpointAddress(connAddr, realService, realParam);
            }
        }
        messengerForHere = event.getMessenger().getChannelMessenger(group.getPeerGroupID(), null, null);
        for (int prec = highestPrec + 1; prec-- > lowestPrec; ) {
            MessengerEvent newMessenger = new MessengerEvent(event.getSource(), prec == EndpointService.LowPrecedence ? messenger : messengerForHere, connAddr);
            Collection<MessengerEventListener> allML = new ArrayList<MessengerEventListener>(passiveMessengerListeners[prec]);
            for (MessengerEventListener listener : allML) {
                try {
                    if (listener.messengerReady(newMessenger)) {
                        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
                            LOG.fine(newMessenger + " claimed by " + listener);
                        }
                        return true;
                    }
                } catch (Throwable all) {
                    if (Logging.SHOW_WARNING && LOG.isLoggable(Level.WARNING)) {
                        LOG.log(Level.WARNING, "Uncaught Throwable in listener " + listener, all);
                    }
                }
            }
        }
        if (Logging.SHOW_FINE && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Nobody cared about " + event);
        }
        return false;
    }
}
