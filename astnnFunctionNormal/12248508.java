class BackupThread extends Thread {
    public void sendMessage(IRCSelfChannelMessage m) throws IOException {
        String[] recips = new String[] { m.getRecipient().getChannelName() };
        sendPrivMessage(recips, m.getMessageText());
        pushMessage(m);
    }
}
