class BackupThread extends Thread {
    @Override
    public void onMessage(MessageEvent<KEllyBot> event) throws Exception {
        super.onMessage(event);
        manageMessage(new Message(nc, event.getMessage(), event.getUser(), event.getChannel(), Message.MSG));
    }
}
