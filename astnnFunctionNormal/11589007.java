class BackupThread extends Thread {
    public void testTwoLoginOneLogout() throws Exception {
        JRMPAdaptor adaptor = new JRMPAdaptor();
        JRMPConnector connector = new JRMPConnector();
        try {
            adaptor.setAuthenticator(new CerberoAdaptorAuthenticator());
            String jndiName = "jrmp";
            adaptor.setJNDIName(jndiName);
            adaptor.setMBeanServer(m_server);
            adaptor.start();
            connector.connect(jndiName, null);
            String user = "simon";
            String address = InetAddress.getLocalHost().getHostAddress();
            char[] password = user.toCharArray();
            CerberoAuthRequest request = new CerberoAuthRequest(user, address);
            CerberoAuthReply reply = (CerberoAuthReply) connector.login(request);
            String user2 = "cerbero";
            char[] password2 = user2.toCharArray();
            CerberoAuthRequest request2 = new CerberoAuthRequest(user2, address);
            CerberoAuthReply reply2 = (CerberoAuthReply) connector.login(request2);
            PasswordEncryptedObject ticket2 = reply2.getLoginTicket();
            LoginTicket login2 = (LoginTicket) ticket2.decrypt(password2);
            char[] key2 = login2.getKey();
            AuthenticatorTicket auth2 = new AuthenticatorTicket(user2, address, 15 * 1000);
            PasswordEncryptedObject authenticator2 = new PasswordEncryptedObject(auth2, key2);
            ServiceTicket serviceTicket2 = new ServiceTicket(authenticator2, login2.getGrantingTicket());
            CerberoInvocationContext context2 = new CerberoInvocationContext(serviceTicket2);
            connector.logout(context2);
            RemoteMBeanServer server = connector.getRemoteMBeanServer();
            PasswordEncryptedObject ticket = reply.getLoginTicket();
            LoginTicket login = (LoginTicket) ticket.decrypt(password);
            char[] key = login.getKey();
            AuthenticatorTicket auth = new AuthenticatorTicket(user, address, 15 * 1000);
            PasswordEncryptedObject authenticator = new PasswordEncryptedObject(auth, key);
            ServiceTicket serviceTicket = new ServiceTicket(authenticator, login.getGrantingTicket());
            CerberoInvocationContext context = new CerberoInvocationContext(serviceTicket);
            connector.setInvocationContext(context);
            try {
                server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
            } finally {
                connector.logout(context);
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
}
