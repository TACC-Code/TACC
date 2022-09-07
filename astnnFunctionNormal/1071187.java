class BackupThread extends Thread {
    private boolean check(int userId, String objectName, String action, String objectType) throws DataAccessException {
        boolean allowed = false;
        Connection connection;
        ConfigManager settings;
        ResultSet results;
        Statement st;
        String tableGroup;
        String tableMembership;
        String tablePermission;
        String tablePermissionGroup;
        String ownerOrganisation;
        String localSQL;
        resetStartTime();
        settings = ConfigManager.getInstance();
        tableGroup = settings.getDefaultGroupTableName();
        tableMembership = settings.getDefaultMembershipTableName();
        tablePermission = objectType.equals(OBJECT_TYPE_BLOCK) ? settings.getDefaultBlockPermissionsTableName() : settings.getDefaultTablePermissionsTableName();
        tablePermissionGroup = settings.getDefaultGroupPermissionsTableName();
        localSQL = "SELECT " + tableMembership + ".\"ownerorganisation\" FROM " + tableMembership + " WHERE " + tableMembership + ".\"primary\" = " + userId;
        m_lastSQL = localSQL;
        if (m_logger.isDebugEnabled()) {
            m_logger.debug(m_lastSQL);
        }
        connection = null;
        ownerOrganisation = null;
        try {
            connection = getConnection();
            st = getStatement(connection);
            results = executeQueryAsync(st, localSQL);
            while (results.next() == true) {
                ownerOrganisation = results.getString("ownerorganisation");
            }
            localSQL = "SELECT " + tablePermission + ".\"read\", " + tablePermission + ".\"write\", " + tablePermission + ".\"delete\" FROM " + tablePermission + ", " + tablePermissionGroup + ", " + tableGroup + " WHERE " + tablePermission + ".\"fkgroup\" = " + tableGroup + ".\"primary\" AND " + tablePermissionGroup + ".\"fkgroup\" = " + tableGroup + ".\"primary\" AND " + tablePermissionGroup + ".\"fkpersonmember\" = " + userId + " AND " + tablePermission + ".\"" + objectType + "\" = '" + objectName + "' AND " + tablePermission + ".\"ownerorganisation\" = '" + ownerOrganisation + "';";
            m_lastSQL = localSQL;
            if (m_logger.isDebugEnabled()) {
                m_logger.debug(m_lastSQL);
            }
            results = executeQueryAsync(st, localSQL);
            while (results.next() == true) {
                allowed |= results.getBoolean(action);
            }
        } catch (Exception except) {
            m_logger.error(except, except);
            throw new DataAccessException("Unable to check security permissions", except);
        } finally {
            closeConnection(connection);
        }
        return allowed;
    }
}
