class BackupThread extends Thread {
    private synchronized void openFile() throws IOException, ArchivingException {
        try {
            fileName = buidFileName(tableName);
            logger.info("open file " + getLocalFilePath());
            final FileOutputStream stream = new FileOutputStream(new File(getLocalFilePath()));
            channel = stream.getChannel();
            if (dbProxy.getDataBase().getDbConn().getDbType() == ConfigConst.TDB_ORACLE) {
                exportFileToDB(fileName);
            }
        } catch (final IOException e) {
            logger.error("ERROR !! " + "\r\n" + "\t Origin : \t " + "FileTools.initFile" + "\r\n" + "\t Reason : \t " + e.getClass().getName() + "\r\n" + "\t Description : \t " + e.getMessage() + "\r\n" + "\t Additional information : \t " + "" + "\r\n");
            e.printStackTrace();
            throw e;
        }
    }
}
