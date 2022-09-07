class BackupThread extends Thread {
    protected Context getResource2ServerInitialContext() throws Exception {
        if (resource2ServerJndiProps == null) {
            URL url = ClassLoader.getSystemResource("jndi.properties");
            resource2ServerJndiProps = new java.util.Properties();
            resource2ServerJndiProps.load(url.openStream());
            String jndiHost = System.getProperty("jbosstest.resource2.server.host", "localhost");
            String jndiUrl = "jnp://" + jndiHost + ":1099";
            resource2ServerJndiProps.setProperty("java.naming.provider.url", jndiUrl);
        }
        return new InitialContext(resource2ServerJndiProps);
    }
}
