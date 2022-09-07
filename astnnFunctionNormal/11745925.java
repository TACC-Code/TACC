class BackupThread extends Thread {
    public void connect() throws XmppException {
        StartTLSSocketStreamSource tls;
        if (isUsed() == true) {
            reset();
        }
        setUsed();
        Stream stream = streamUtils.getStream();
        try {
            tls = new StartTLSSocketStreamSource(host, port);
            stream.connect(tls);
            stream.open(timeout);
            LoginManager lm = new LoginManager(streamUtils, pwd, tls);
            lm.login();
        } catch (Exception e) {
            throw new XmppException(e);
        }
        streamUtils.startSessionLoop(false);
    }
}
