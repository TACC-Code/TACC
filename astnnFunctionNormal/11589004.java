class BackupThread extends Thread {
    public void testBadAddressContext() throws Exception {
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
            String address = InetAddress.getLocalHost().getHostAddress();
            char[] password = user.toCharArray();
            CerberoAuthRequest request = new CerberoAuthRequest(user, address);
            CerberoAuthReply reply = (CerberoAuthReply) connector.login(request);
            PasswordEncryptedObject ticket = reply.getLoginTicket();
            LoginTicket login = (LoginTicket) ticket.decrypt(password);
            char[] key = login.getKey();
            AuthenticatorTicket auth = new AuthenticatorTicket(user, null, 15 * 1000);
            PasswordEncryptedObject authenticator = new PasswordEncryptedObject(auth, key);
            ServiceTicket serviceTicket = new ServiceTicket(authenticator, login.getGrantingTicket());
            CerberoInvocationContext context = new CerberoInvocationContext(serviceTicket);
            connector.setInvocationContext(context);
            try {
                server.getAttribute(new ObjectName("JMImplementation:type=MBeanServerDelegate"), "MBeanServerId");
                fail("Should not be able to call with bad user invocation context");
            } catch (SecurityException x) {
            } finally {
                auth = new AuthenticatorTicket(user, address, 15 * 1000);
                authenticator = new PasswordEncryptedObject(auth, key);
                serviceTicket = new ServiceTicket(authenticator, login.getGrantingTicket());
                context = new CerberoInvocationContext(serviceTicket);
                connector.logout(context);
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
}
