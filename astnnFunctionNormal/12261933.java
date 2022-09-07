class BackupThread extends Thread {
    @Override
    public Object getSource(ContentType contentType) {
        switch(contentType) {
            case ByteStream:
            case XML:
            case ResultSet:
                throw new XformationException("Content type: " + contentType + " is not supported.");
        }
        if (readerForFaucet != null) return readerForFaucet;
        this.writer = new PipedWriter();
        try {
            this.readerForFaucet = new PipedReader(this.writer);
        } catch (IOException e) {
            throw new XformationException("Unable to create hooked piped reader/writer", e);
        }
        this.readerListForDisposal.add(this.readerForFaucet);
        if (logger.isTraceEnabled()) logger.trace("Created reader " + readerForFaucet + " and writer " + writer);
        return readerForFaucet;
    }
}
