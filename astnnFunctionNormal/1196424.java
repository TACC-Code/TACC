class BackupThread extends Thread {
    public void test_migrateFormOldVersionTable_setEstablishedProjectID_addNewProject() throws SQLException {
        _writeTestStart("Already existing version table. Set established project ID and add a new Project");
        String versionTable = _getNewVersionTable();
        String versionTableOld = this.config.getVersionTable();
        assertFalse(Execute.tableExists(getConnection(), versionTable));
        _createOldStyleVersionTable(4);
        _tableExists(versionTableOld);
        assertTrue(1 == _getNumberOfRows(this.config, versionTableOld));
        assertFalse(_columnExists(ConfigStore.PROJECT_FIELD_NAME, versionTableOld));
        assertTrue(_columnExists(ConfigStore.VERSION_FIELD_NAME, versionTableOld));
        this.config.setEstablishedProjectID("db.migrations");
        this.config.setPackageName(ProjectX.PACKAGE);
        this.config.setProjectID(ProjectX.PROJEXT_ID);
        Engine.migrate(this.config);
        assertTrue(Execute.tableExists(getConnection(), versionTable));
        assertTrue(3 == _getNumberOfRows(this.config, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.PROJECT_FIELD_NAME, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.VERSION_FIELD_NAME, versionTable));
        assertTrue(2 <= VersionQuery.getVersion(this.config, "com.eroi.migrate.version"));
        assertTrue(2 == VersionQuery.getVersion(this.config, ProjectX.PROJEXT_ID));
        assertTrue(4 == VersionQuery.getVersion(this.config, "db.migrations"));
        assertTrue(Execute.tableExists(getConnection(), ProjectX.X1_TABLE_NAME));
        assertFalse(Execute.tableExists(getConnection(), Migration_1.TABLE_NAME));
        assertFalse(Execute.tableExists(getConnection(), Migration_5.PARENT_TABLE_NAME));
        assertFalse(Execute.tableExists(getConnection(), Migration_5.CHILD_TABLE_NAME));
        Engine.migrate(this.config, 0);
        assertTrue(Execute.tableExists(getConnection(), versionTable));
        assertTrue(3 == _getNumberOfRows(this.config, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.PROJECT_FIELD_NAME, versionTable));
        assertTrue(Execute.columnExists(getConnection(), ConfigStore.VERSION_FIELD_NAME, versionTable));
        assertTrue(2 <= VersionQuery.getVersion(this.config, "com.eroi.migrate.version"));
        assertTrue(0 == VersionQuery.getVersion(this.config, ProjectX.PROJEXT_ID));
        assertTrue(4 == VersionQuery.getVersion(this.config, "db.migrations"));
        assertFalse(Execute.tableExists(getConnection(), ProjectX.X1_TABLE_NAME));
        _writeTestPass();
    }
}
