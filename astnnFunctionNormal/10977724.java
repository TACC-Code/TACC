class BackupThread extends Thread {
    public static void main(final String[] args) {
        PropertyConfigurator.configureAndWatch("/log4j.xml");
        final ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
        try {
            logger.debug("Starting the controller");
            threadExecutor.submit((Callable<Integer>) new PluginController("/config/writer.xml")).get();
        } catch (final InterruptedException e) {
            logger.error(FATAL_ERROR, e);
            System.exit(-1);
        } catch (final ExecutionException e) {
            logger.error(FATAL_ERROR, e);
            System.exit(-2);
        }
    }
}
