class BackupThread extends Thread {
    @Override
    public void onAction(ActionEvent<KEllyBot> event) throws Exception {
        super.onAction(event);
        manageMessage(new Message(nc, event.getAction(), event.getUser(), event.getChannel(), Message.ACTION));
    }
}
