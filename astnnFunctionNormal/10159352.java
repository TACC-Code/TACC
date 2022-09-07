class BackupThread extends Thread {
    public void actionPerformed(ActionEvent e) {
        LeaveChannelMessage message = new LeaveChannelMessage();
        message.channel = Application.getChannel();
        message.id = Application.getId();
        try {
            SocketClient.getInstance().send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
