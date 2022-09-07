class BackupThread extends Thread {
    public InputStream getTableLists() throws IOException {
        URL url = DBMigrateConfig.class.getResource("dbmigratetable.xml");
        return url.openStream();
    }
}
