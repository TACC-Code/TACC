class BackupThread extends Thread {
    protected Context getResource1ServerInitialContext() throws Exception {
        if (resource1ServerJndiProps == null) {
            URL url = ClassLoader.getSystemResource("jndi.properties");
            resource1ServerJndiProps = new java.util.Properties();
            resource1ServerJndiProps.load(url.openStream());
            String jndiHost = System.getProperty("jbosstest.resource1.server.host", "localhost");
            String jndiUrl = "jnp://" + jndiHost + ":1099";
            resource1ServerJndiProps.setProperty("java.naming.provider.url", jndiUrl);
        }
        return new InitialContext(resource1ServerJndiProps);
    }
}
