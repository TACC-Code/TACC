class BackupThread extends Thread {
    public static void processConnectionResponse(MessagesConsumer server, ClientThread thread, AbstractMessage message) throws Exception {
        ConnectionResponse response = (ConnectionResponse) message;
        String salt = response.getSalt();
        LoginRequest request = new LoginRequest();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        request.setLogin(Main.getInstance().getNetworkManager().getLogin());
        String password = byteArrayToHexString(digest.digest((salt + Main.getInstance().getNetworkManager().getPassword()).getBytes()));
        server.getLogger().info("Sending password: '" + password + "'");
        request.setPassword(password);
        thread.sendMessage(request);
    }
}
