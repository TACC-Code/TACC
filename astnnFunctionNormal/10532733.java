class BackupThread extends Thread {
            @Override
            public void uncaughtException(final Thread t, final Throwable e) {
                writeThreadThrowable(t, e);
                if (_defaultUncaughtExceptionHandler != null) {
                    _defaultUncaughtExceptionHandler.uncaughtException(t, e);
                }
            }
}
