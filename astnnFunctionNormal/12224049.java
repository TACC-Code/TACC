class BackupThread extends Thread {
    public void login(InetSocketAddress loginServerAddr, String account, char[] password, AsyncCallbackListener callback) {
        try {
            Boolean isLoggingIn = (Boolean) getProperty(KEY_IS_LOGGING_IN);
            if (!isLoggingIn.booleanValue()) {
                setProperty(KEY_IS_LOGGING_IN, Boolean.TRUE);
                setProperty(KEY_LOGIN_CALLBACK, callback);
                fireLoggingIn();
                networkSystem.getDatagramReceiver().setRunning(true);
                networkSystem.getDatagramSender().setRunning(true);
                setProperty(KEY_ACCOUNT, account);
                Socket temp = new Socket();
                temp.bind(new InetSocketAddress(0));
                temp.connect(loginServerAddr);
                Login login = new Login();
                login.account = account;
                login.password = password;
                Packet[] array = Converter.getInstance().convertAndSplit(new Packet(), login);
                for (int i = 0; i < array.length; i++) {
                    temp.getOutputStream().write(array[i].toByteArray());
                }
                temp.getOutputStream().flush();
                byte[] data = new byte[Packet.PACKET_SIZE];
                temp.getInputStream().read(data);
                SessionKey rsp = (SessionKey) Converter.getInstance().revert(new Packet[] { Packet.fromByteArray(data) });
                if (!Results.succeeded(rsp.result)) {
                    throw new PeterHiException(rsp.result);
                }
                setProperty(KEY_KEY, rsp.key);
                UdpContact uc = new UdpContact();
                uc.account = (String) getProperty(KEY_ACCOUNT);
                uc.key = (String) getProperty(KEY_KEY);
                networkSystem.udpPutAll(Converter.getInstance().convertAndSplit(new Packet(), uc), loginServerAddr);
                Properties props = new Properties();
                props.put("host", "localhost");
                props.put("port", "8001");
                simpleClient.login(props);
            }
        } catch (Exception ex) {
            setProperty(KEY_SGS_LOGGED_IN, Boolean.FALSE);
            setProperty(KEY_UDP_LOGGED_IN, Boolean.FALSE);
            setProperty(KEY_IS_LOGGING_IN, Boolean.FALSE);
            setProperty(KEY_ACCOUNT, "");
            fireLoginFailed(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}
