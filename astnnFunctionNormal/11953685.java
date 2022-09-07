class BackupThread extends Thread {
    public Long removeAllEntities() {
        long numberOfEntitiesRemoved = 0;
        final EntityManager entityManager = getEntityManager();
        EntityTransaction tx = null;
        try {
            synchronized (this) {
                tx = entityManager.getTransaction();
                if (!tx.isActive()) {
                    tx.begin();
                }
            }
            final Query query = entityManager.createQuery("delete from java.lang.Object");
            numberOfEntitiesRemoved = query.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            log.error("Removal of entities failed", e);
        }
        log.debug(numberOfEntitiesRemoved + " entities removed");
        return numberOfEntitiesRemoved;
    }
}
