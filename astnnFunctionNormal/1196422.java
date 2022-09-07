class BackupThread extends Thread {
    public void test_migrateFormOldVersionTable_setEstablishedProjectID_switchToNewStyle() throws SQLException {
        _writeTestStart("Already existing version table. Set established project ID and switch to new style");
        String versionTable = _getNewVersionTable();
        String versionTableOld = this.config.getVersionTable();
        assertFalse(Execute.tableExists(getConnection(), versionTable));
        _createOldStyleVersionTable(4);
        _tableExists(versionTableOld);
        assertTrue(1 == _getNumberOfRows(this.config, versionTableOld));
        assertFalse(_columnExists(ConfigStore.PROJECT_FIELD_NAME, versionTableOld));
        assertTrue(_columnExists(ConfigStore.VERSION_FIELD_NAME, versionTableOld));
        this.config.setEstablishedProjectID("db.migrations");
        Engine.migrate(this.config, 5);
        assertTrue(Execute.tableExists(getConnection(), versionTable));
        assertTrue(2 == _getNumberOfRows(this.config, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.PROJECT_FIELD_NAME, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.VERSION_FIELD_NAME, versionTable));
        assertTrue(2 <= VersionQuery.getVersion(this.config, "com.eroi.migrate.version"));
        assertTrue(5 == VersionQuery.getVersion(this.config, "db.migrations"));
        assertFalse(Execute.tableExists(getConnection(), Migration_1.TABLE_NAME));
        assertTrue(Execute.tableExists(getConnection(), Migration_5.PARENT_TABLE_NAME));
        assertTrue(Execute.tableExists(getConnection(), Migration_5.CHILD_TABLE_NAME));
        Engine.migrate(this.config, 0);
        assertTrue(Execute.tableExists(getConnection(), versionTable));
        assertTrue(2 == _getNumberOfRows(this.config, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.PROJECT_FIELD_NAME, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.VERSION_FIELD_NAME, versionTable));
        assertTrue(2 <= VersionQuery.getVersion(this.config, "com.eroi.migrate.version"));
        assertTrue(0 == VersionQuery.getVersion(this.config, "db.migrations"));
        assertFalse(Execute.tableExists(getConnection(), Migration_1.TABLE_NAME));
        assertFalse(Execute.tableExists(getConnection(), Migration_5.PARENT_TABLE_NAME));
        assertFalse(Execute.tableExists(getConnection(), Migration_5.CHILD_TABLE_NAME));
        _writeTestPass();
    }
}
