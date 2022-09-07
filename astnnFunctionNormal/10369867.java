class BackupThread extends Thread {
    private void createDbIndices() throws CannotConnectToDatabaseException {
        final String showQueryAc = "SHOW INDEX FROM actor WHERE Key_name LIKE 'IDX_Actor_Name';";
        final String createQueryAc = "CREATE INDEX IDX_Actor_Name ON actor(name);";
        Session s = ModelManager.getInstance().getCurrentSession();
        Transaction tx = null;
        int result;
        try {
            tx = s.beginTransaction();
            result = s.createSQLQuery(showQueryAc).executeUpdate();
            if (result == 0) {
                s.createSQLQuery(createQueryAc).executeUpdate();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Successfully created index on name field of actor table.");
                }
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Index on name field of actor table already exists. No need to create.");
                }
            }
            s.clear();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to create index on actor table - transaction was rolled back.", he);
            }
            throw he;
        } finally {
            s.close();
        }
        final String showQueryCE = "SHOW KEYS FROM contentelement WHERE Key_name LIKE 'IDX_CE_Title';";
        final String createQueryCE = "CREATE INDEX IDX_CE_Title ON contentelement(name);";
        s = ModelManager.getInstance().getCurrentSession();
        try {
            tx = s.beginTransaction();
            result = s.createSQLQuery(showQueryCE).executeUpdate();
            if (result == 0) {
                s.createSQLQuery(createQueryCE).executeUpdate();
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Successfully created index on name field of contentelement table.");
                }
            } else {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Index on name field of contentelement table already exists. No need to create.");
                }
            }
            s.clear();
            tx.commit();
        } catch (final HibernateException he) {
            tx.rollback();
            if (LOGGER.isErrorEnabled()) {
                LOGGER.error("Failed to create index on contentelement table - transaction was rolled back.", he);
            }
            throw he;
        } finally {
            s.close();
        }
    }
}
