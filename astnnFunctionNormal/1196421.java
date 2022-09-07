class BackupThread extends Thread {
    public void test_migrateFormOldVersionTable_continueOldStyle() throws SQLException {
        _writeTestStart("Already existing version table. Continue old style");
        String versionTable = _getNewVersionTable();
        String versionTableOld = this.config.getVersionTable();
        assertFalse(Execute.tableExists(versionTable));
        _createOldStyleVersionTable(4);
        _tableExists(versionTableOld);
        assertTrue(1 == _getNumberOfRows(defaultConfiguration, versionTableOld));
        assertFalse(_columnExists(ConfigStore.PROJECT_FIELD_NAME, versionTableOld));
        assertTrue(_columnExists(ConfigStore.VERSION_FIELD_NAME, versionTableOld));
        Engine.migrate(5);
        assertTrue(Execute.tableExists(versionTable));
        assertTrue(2 == _getNumberOfRows(defaultConfiguration, versionTable));
        assertTrue(Execute.columnExists(ConfigStore.PROJECT_FIELD_NAME, versionTable));
        assertTrue(Execute.columnExists(ConfigStore.VERSION_FIELD_NAME, versionTable));
        assertTrue(2 <= VersionQuery.getVersion(defaultConfiguration, "com.eroi.migrate.version"));
        assertTrue(5 == VersionQuery.getVersion(defaultConfiguration, ConfigStore.PROJECT_ID_UNSPECIFIED));
        assertFalse(Execute.tableExists(Migration_1.TABLE_NAME));
        assertTrue(Execute.tableExists(Migration_5.PARENT_TABLE_NAME));
        assertTrue(Execute.tableExists(Migration_5.CHILD_TABLE_NAME));
        Engine.migrate(0);
        assertTrue(Execute.tableExists(versionTable));
        assertTrue(2 == _getNumberOfRows(defaultConfiguration, versionTable));
        assertTrue(Execute.columnExists(ConfigStore.PROJECT_FIELD_NAME, versionTable));
        assertTrue(Execute.columnExists(ConfigStore.VERSION_FIELD_NAME, versionTable));
        assertTrue(2 <= VersionQuery.getVersion(defaultConfiguration, "com.eroi.migrate.version"));
        assertTrue(0 == VersionQuery.getVersion(defaultConfiguration, ConfigStore.PROJECT_ID_UNSPECIFIED));
        assertFalse(Execute.tableExists(Migration_1.TABLE_NAME));
        assertFalse(Execute.tableExists(Migration_5.PARENT_TABLE_NAME));
        assertFalse(Execute.tableExists(Migration_5.CHILD_TABLE_NAME));
        _writeTestPass();
    }
}
