class BackupThread extends Thread {
    @Override
    protected void tearDown() throws Exception {
        logger.info("tearDown() - start");
        logger.info("tearDown() - setting converter, readers and writers to null");
        converter = null;
        if (inReader != null) inReader.close();
        if (outWriter != null) outWriter.close();
        inReader = null;
        outWriter = null;
        logger.info("tearDown() - end");
    }
}
