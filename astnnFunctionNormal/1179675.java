class BackupThread extends Thread {
    public static Channel getFactionChatChannel(String name) {
        try {
            return AppContext.getChannelManager().getChannel(chatPrefix + name);
        } catch (NameNotBoundException e) {
            return null;
        }
    }
}
