class BackupThread extends Thread {
    @Before
    public void setUpRepository() throws Exception {
        repo = (Repository) new TransientRepository();
        session = repo.login(new SimpleCredentials("a", "b".toCharArray()));
        ClassLoader loader = TestReferenceHistory.class.getClassLoader();
        URL url = loader.getResource("logger.properties");
        if (url == null) {
            url = loader.getResource("/logger.properties");
        }
        LogManager.getLogManager().readConfiguration(url.openStream());
    }
}
