class BackupThread extends Thread {
    public static void writeStringToFile(final String content, final File destination) throws FileIOException {
        logger.debug("entering writeStringToFile");
        if (destination.exists() && !destination.canWrite()) {
            String msg = "Cannot write to file " + destination.getAbsolutePath() + " File already exists and cannot be overwritten";
            logger.warn(msg);
            throw new FileIOException(msg);
        }
        Writer fWriter = null;
        PrintWriter pWriter = null;
        try {
            logger.debug("Attempting to write to file " + destination.getAbsolutePath());
            fWriter = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(destination)), ENCODING);
            pWriter = new PrintWriter(fWriter);
            pWriter.print(content);
            pWriter.flush();
            pWriter.close();
            logger.debug("Successful write to file " + destination.getAbsolutePath());
        } catch (IOException e) {
            String msg = "Error occured while writing to file " + destination.getAbsolutePath();
            logger.error(msg, e);
            FileIOException fex = new FileIOException(msg, e);
            throw fex;
        } finally {
            if (fWriter != null) {
                try {
                    fWriter.close();
                    logger.debug("Successfully closed file " + destination.getAbsolutePath());
                } catch (IOException e) {
                    String msg = "Error occured while closing file " + destination.getAbsolutePath();
                    logger.error(msg, e);
                    FileIOException fex = new FileIOException(msg, e);
                    throw fex;
                }
            }
        }
        logger.debug("exiting writeStringToFile");
    }
}
