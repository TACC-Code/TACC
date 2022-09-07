class BackupThread extends Thread {
    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = DBMigrateConfig.class.getResource("oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = DBMigrateConfig.class.getResource("sql2k.xml");
        }
        return url.openStream();
    }
}
