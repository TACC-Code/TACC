class BackupThread extends Thread {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        myLog = new MockLogger();
        myAction = new LoginAction(new DummyAuthenticator(USERS));
        myAction.setLog(myLog);
        myUsernameChannel = myAction.getChannel(LoginAction.CHANNEL_USERNAME);
        myPasswordChannel = myAction.getChannel(LoginAction.CHANNEL_PASSWORD);
        myUserChannel = myAction.getChannel(LoginAction.CHANNEL_USER);
    }
}
