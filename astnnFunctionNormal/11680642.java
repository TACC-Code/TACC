class BackupThread extends Thread {
    @Override
    public boolean lockDigitalObject(String uuid, Long id, String description) throws DatabaseException {
        PreparedStatement updateSt = null;
        boolean successful = false;
        try {
            if (id != null) {
                updateSt = getConnection().prepareStatement(INSERT_NEW_DIGITAL_OBJECTS_LOCK);
                updateSt.setString(1, uuid);
                updateSt.setString(2, description);
                updateSt.setLong(3, id);
            } else {
                updateSt = getConnection().prepareStatement(UPDATE_DIGITAL_OBJECTS_TIMESTAMP_DESCRIPTION);
                updateSt.setString(1, description);
                updateSt.setString(2, uuid);
            }
        } catch (SQLException e) {
            LOGGER.error("Could not get insert statement", e);
        } finally {
            closeConnection();
        }
        int modified = 0;
        try {
            modified = updateSt.executeUpdate();
            if (modified == 1) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + updateSt + "\"");
                successful = true;
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback! Queries: \"" + updateSt + "\"");
                successful = false;
            }
        } catch (SQLException e) {
            LOGGER.error("Query: " + updateSt, e);
        } finally {
            closeConnection();
        }
        return successful;
    }
}
