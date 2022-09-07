class BackupThread extends Thread {
    public static PrintWriter getFilePrintWriter(XMLObject xo, String parserName, String attributeName) throws XMLParseException {
        if (xo.hasAttribute(attributeName)) {
            final File logFile = getFileHandle(xo, attributeName);
            boolean allowOverwrite = false;
            if (xo.hasAttribute(LoggerParser.ALLOW_OVERWRITE_LOG)) {
                allowOverwrite = xo.getBooleanAttribute(LoggerParser.ALLOW_OVERWRITE_LOG);
            }
            if (System.getProperty("log.allow.overwrite") != null) {
                allowOverwrite = Boolean.parseBoolean(System.getProperty("log.allow.overwrite", "false"));
            }
            if (logFile.exists() && !allowOverwrite) {
                throw new XMLParseException("\nThe log file " + logFile.getName() + " already exists in the working directory." + "\nTo allow it to be overwritten, use the '-overwrite' command line option when running" + "\nBEAST or select the option in the Run Options dialog box as appropriate.");
            }
            try {
                return new PrintWriter(new FileOutputStream(logFile));
            } catch (FileNotFoundException fnfe) {
                throw new XMLParseException("File '" + logFile.getAbsolutePath() + "' can not be opened for " + parserName + " element.");
            }
        }
        return new PrintWriter(System.out);
    }
}
