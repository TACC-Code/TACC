class BackupThread extends Thread {
    @Override
    public Object consume(ISink previousSink) {
        if (!hookedUpFaucet) throw new XformationException("Pipeline item has not been set up correctly:" + "faucet has not been set");
        if (previousSink instanceof ISinkTemplate) {
            if (((ISinkTemplate) previousSink).isClone()) return null;
        }
        if (faucet instanceof IPipelineItem) ((IPipelineItem) faucet).consume(this);
        createPipeline();
        reader = (Reader) faucet.getSource(ContentType.CharStream);
        getSource(ContentType.CharStream);
        if (sink.get() instanceof IPipelineItem) ((IPipelineItem) sink.get()).handleMuxSwitch();
        final ISink fPreviousSink = previousSink;
        ExecutorsHelper.getInstance().executeInProc(new Runnable() {

            @Override
            public void run() {
                try {
                    int count;
                    char[] buffer = new char[8 * 1024];
                    while ((count = reader.read(buffer)) != -1) {
                        char[] transformation = transformBuffer(buffer, 0, count);
                        if (logger.isTraceEnabled()) logger.trace("Read '" + new String(buffer, 0, count) + "' from " + reader + " writing '" + new String(transformation) + "' to writer " + writer);
                        writer.write(transformation, 0, transformation.length);
                        while (shouldChangePipeline()) {
                            writer.close();
                            if (logger.isTraceEnabled()) logger.trace("About to switch pipeline");
                            switchPipeline();
                            transformation = transformBuffer(buffer, 0, 0);
                            if (logger.isTraceEnabled()) logger.trace("Writing  additional '" + new String(transformation) + "' to writer " + writer);
                            writer.write(transformation, 0, transformation.length);
                        }
                    }
                    char[] transformation = transformBuffer(buffer, 0, count);
                    if (logger.isTraceEnabled()) logger.trace("Writing  final '" + new String(transformation) + "' to writer " + writer);
                    writer.write(transformation, 0, transformation.length);
                    writer.close();
                    if (fPreviousSink instanceof IPipelineItem) {
                        IPipelineItem pipelineSink = (IPipelineItem) fPreviousSink;
                        pipelineSink.endMux();
                    }
                    logger.debug("Executable for the reader pipeline will now exit.");
                } catch (Throwable t) {
                    logger.error("Error during pipeline thread execution.", t);
                }
            }
        });
        return null;
    }
}
