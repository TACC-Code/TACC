class BackupThread extends Thread {
    @Override
    public void createData(PersistentFactory persistentFactory, Connection connection, Logger logger, ModelRequest request) throws ModelException, PersistenceException, SQLException {
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.user", "root", "*");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.group", "root", "*");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.groupmembers", "root", "*");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.user", "anonymous", "L");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.group", "anonymous", "L");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.groupmembers", "anonymous", "L");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.user", "guest", "L");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.group", "guest", "L");
        createInstanceSecurity("de.iritgo.aktera.persist.defaultpersist.DefaultPersistent", "keel.groupmembers", "guest", "L");
        update("INSERT INTO keelgroups (GroupName, Descrip) values ('admin', 'Administrator')");
        update("INSERT INTO keelgroups (GroupName, Descrip) values ('manager', 'Manager')");
        update("INSERT INTO keelgroups (GroupName, Descrip) values ('user', 'User')");
        update("INSERT INTO keelusers (uniqid, userName, email, passwd)" + " values (0, 'anonymous', 'anonymous@unknown', '" + StringTools.digest("") + "')");
        update("INSERT INTO keelusers (uniqid, userName, email, passwd)" + " values (1, 'admin', 'admin@unknown', '" + StringTools.digest("admin") + "')");
        update("INSERT INTO keelusers (uniqid, userName, email, passwd)" + " values (2, 'manager', 'manager@unknown', '" + StringTools.digest("manager") + "')");
        update("ALTER SEQUENCE keelusers_uniqid_seq START WITH 3");
        update("INSERT INTO keelgroupmembers (UniqId, GroupName) values (0, 'anonymous')");
        update("INSERT INTO keelgroupmembers (UniqId, GroupName) values (1, 'root')");
        update("INSERT INTO keelgroupmembers (UniqId, GroupName) values (1, 'admin')");
        update("INSERT INTO keelgroupmembers (UniqId, GroupName) values (1, 'manager')");
        update("INSERT INTO keelgroupmembers (UniqId, GroupName) values (1, 'user')");
        update("INSERT INTO keelgroupmembers (UniqId, GroupName) values (2, 'manager')");
        update("INSERT INTO keelgroupmembers (UniqId, GroupName) values (2, 'user')");
        update("INSERT INTO AkteraGroup (name, protect, title, visible) values ('" + AkteraGroup.GROUP_NAME_ADMINISTRATOR + "', true, '$Aktera:administrators', false)");
        update("INSERT INTO AkteraGroup (name, protect, title, visible) values ('" + AkteraGroup.GROUP_NAME_MANAGER + "', true, '$Aktera:managers', false)");
        update("INSERT INTO AkteraGroup (name, protect, title, visible) values ('" + AkteraGroup.GROUP_NAME_USER + "', true, '$Aktera:users', true)");
        update("INSERT INTO AkteraGroupEntry (groupId, userId, position) values (1, 1, 1)");
        update("INSERT INTO AkteraGroupEntry (groupId, userId, position) values (2, 2, 1)");
    }
}
