class BackupThread extends Thread {
    protected void localStartBoundaryImpl(Object inputSource) {
        reader = (Reader) faucet.getSource(ContentType.CharStream);
        getSource(ContentType.CharStream);
        try {
            int count;
            char[] buffer = new char[8 * 1024];
            if (logger.isTraceEnabled()) logger.trace("Using input stream " + reader + " for the stream transformation (start of pipeline).");
            while ((count = reader.read(buffer)) != -1) {
                String readInput = new String(buffer, 0, count);
                if (logger.isTraceEnabled()) logger.trace("Read " + readInput + " from reader " + reader + ", writing to writer " + writer);
                writer.write(buffer, 0, count);
            }
            writer.close();
        } catch (Throwable t) {
            logger.error("Error during pipeline thread execution.", t);
        }
    }
}
