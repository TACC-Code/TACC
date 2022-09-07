class BackupThread extends Thread {
    public void testBadUserBadAddressRequest() throws Exception {
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
            CerberoAuthRequest badRequest = new CerberoAuthRequest(null, null);
            try {
                connector.login(badRequest);
                fail("Should not login with null user");
            } catch (SecurityException x) {
            }
        } finally {
            connector.close();
            adaptor.stop();
        }
    }
}
