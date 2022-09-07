class BackupThread extends Thread {
    protected void init(Class<T> cls, Key... keys) {
        readDataSource = Execution.getDataSourceName(cls.getName(), true);
        writeDataSource = Execution.getDataSourceName(cls.getName(), false);
        if (readDataSource == null) {
            readDataSource = writeDataSource;
        }
        if (writeDataSource == null) {
            writeDataSource = readDataSource;
        }
    }
}
