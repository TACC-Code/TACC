class BackupThread extends Thread {
    public void mouseReleased(MouseEvent e) {
        String userName = name.getText();
        String userPwd = new String(pwd.getPassword());
        ClientMain client = new ClientMain("localhost", 8800);
        client.startClient();
        String loginMsg = "<type>login</type><name>" + userName + "</name><pwd>" + userPwd + "</pwd>";
        Message.sendMessage(loginMsg, MainChannel.getChannel());
    }
}
