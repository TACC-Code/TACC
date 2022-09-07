class BackupThread extends Thread {
    protected boolean removeConnection(String address, int type) {
        try {
            String app = sm.getApplicationUsedByUser(address);
            boolean appKnows;
            switch(type) {
                case NORMAL:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUser(app, address);
                    break;
                case ABNORMAL:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserAbnormal(app, address);
                    break;
                case PING:
                    if (VERBOSE) System.out.println("Sending PING_DISCONNECTION byte");
                    sm.getAssociatedProcess(address).write(ConnectionThread.PING_DISCONNECTION);
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserPing(app, address);
                    break;
                case SYSBAN:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserSysBan(app, address, BanManager.getInstance().tellMeWhy(address));
                    break;
                case APPBAN:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUserAppBan(app, address, BanManager.getInstance().tellMeWhy(address));
                    break;
                case REGISTRATION:
                    if (VERBOSE) System.out.println("Sending AUTH_FAILED byte");
                    sm.getAssociatedProcess(address).write(ConnectionThread.AUTH_FAILED);
                default:
                    appKnows = ThreadManager.getInstance().notifyDisconnessionUser(app, address);
                    break;
            }
            sm.getAssociatedProcess(address).disconnect();
            boolean deleted = sm.removeConnection(address);
            if (deleted) MainListener.getInstance().displayTrayMessage("User id " + address + " disconnected", false);
            return (deleted && appKnows);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
