class BackupThread extends Thread {
    private void attachLogger() {
        GLOBAL_LOGGER.setUseParentHandlers(true);
        GLOBAL_LOGGER.setLevel(Level.INFO);
        GLOBAL_LOGGER.addHandler(_loggerHandler);
        _defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                writeThreadThrowable(t, e);
                if (_defaultUncaughtExceptionHandler != null) {
                    _defaultUncaughtExceptionHandler.uncaughtException(t, e);
                }
            }
        });
    }
}
