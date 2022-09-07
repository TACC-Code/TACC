class BackupThread extends Thread {
    public void removeAllRecordsInTable(final String tableName) {
        long numberOfEntitiesRemoved = 0;
        final EntityManager entityManager = getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            Query query = entityManager.createNativeQuery("delete from " + tableName);
            numberOfEntitiesRemoved = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            log.error("Removal of entities failed", e);
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
        log.info(numberOfEntitiesRemoved + " records explicitely deleted in table " + tableName.toUpperCase());
    }
}
