class BackupThread extends Thread {
    public static void createDefaultConfiguration(File configDir) {
        if (!configDir.isDirectory()) {
            if (!configDir.mkdirs()) {
                return;
            }
        }
        DBBroker broker = null;
        Subject subject = null;
        try {
            broker = BrokerPool.getInstance().getActiveBroker();
            subject = broker.getSubject();
            broker.setSubject(broker.getBrokerPool().getSecurityManager().getSystemSubject());
            Resource configFile = new Resource(configDir, "config");
            Resource serversFile = new Resource(configDir, "servers");
            Resource readmeFile = new Resource(configDir, "README.txt");
            writeFile("/org/tmatesoft/svn/core/internal/wc/config/config", configFile);
            writeFile("/org/tmatesoft/svn/core/internal/wc/config/servers", serversFile);
            writeFile("/org/tmatesoft/svn/core/internal/wc/config/README.txt", readmeFile);
        } catch (EXistException e) {
            LOG.debug(e);
        } finally {
            if (broker != null && subject != null) broker.setSubject(subject);
        }
    }
}
