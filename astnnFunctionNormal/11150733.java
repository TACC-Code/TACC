class BackupThread extends Thread {
    protected void append() throws IOException {
        Logger rootLogger = LogManager.getRootLogger();
        Appender appender = rootLogger.getAppender(appenderName);
        if (appender == null) {
            log.info("Null appender get by [" + appenderName + "]");
            return;
        }
        if (appender instanceof WriterAppender) {
            Writer writer = new PipedWriter(this.reader);
            WriterAppender writerAppender = (WriterAppender) appender;
            writerAppender.setWriter(writer);
        } else {
            log.info("UnSupport appender [" + appender + "],current support 'WriterAppender' or it's subclass");
        }
    }
}
