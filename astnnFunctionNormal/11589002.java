class BackupThread extends Thread {
    public void testBadAddressRequest() throws Exception {
        JRMPAdaptor adaptor = new JRMPAdaptor();
        JRMPConnector connector = new JRMPConnector();
        try {
            adaptor.setAuthenticator(new CerberoAdaptorAuthenticator());
            String jndiName = "jrmp";
            adaptor.setJNDIName(jndiName);
            adaptor.setMBeanServer(m_server);
            adaptor.start();
            connector.connect(jndiName, null);
            RemoteMBeanServer server = connector.getRemoteMBeanServer();
            String user = "simon";
            CerberoAuthRequest request = new CerberoAuthRequest(user, null);
            char[] password = user.toCharArray();
            CerberoAuthReply reply = (CerberoAuthReply) connector.login(request);
            PasswordEncryptedObject ticket = reply.getLoginTicket();
            LoginTicket login = (LoginTicket) ticket.decrypt(password);
            char[] key = login.getKey();
            AuthenticatorTicket auth = new AuthenticatorTicket(request.getUser(), request.getAddress(), 15 * 1000);
            PasswordEncryptedObject authenticator = new PasswordEncryptedObject(auth, key);
            ServiceTicket serviceTicket = new ServiceTicket(authenticator, login.getGrantingTicket());
            CerberoInvocationContext context = new CerberoInvocationContext(serviceTicket);
            try {
                connector.logout(context);
                fail("Should not be able to logout, address is null");
            } catch (SecurityException x) {
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
}
