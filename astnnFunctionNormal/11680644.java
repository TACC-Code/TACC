class BackupThread extends Thread {
    @Override
    public boolean unlockDigitalObject(String uuid) throws DatabaseException {
        PreparedStatement deleteSt = null;
        boolean successful = false;
        try {
            deleteSt = getConnection().prepareStatement(DELETE_DIGITAL_OBJETCS_LOCK_BY_UUID);
            deleteSt.setString(1, uuid);
        } catch (SQLException e) {
            LOGGER.error("Could not get delete statement", e);
        } finally {
            closeConnection();
        }
        int modified;
        try {
            modified = deleteSt.executeUpdate();
            if (modified == 1) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + deleteSt + "\"");
                successful = true;
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback! Queries: \"" + deleteSt + "\"");
                successful = false;
            }
        } catch (SQLException e) {
            LOGGER.error("Query: " + deleteSt, e);
        } finally {
            closeConnection();
        }
        return successful;
    }
}
