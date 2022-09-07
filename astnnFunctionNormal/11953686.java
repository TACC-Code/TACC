class BackupThread extends Thread {
    @Override
    public <V extends T> Long removeAllEntitiesSpecifiedBy(final Specification<V> specification) {
        Validate.notNull(specification, "Specification parameter cannot be null");
        if (specification.getType().equals(Entity.class)) {
            return removeAllEntities();
        }
        long numberOfRemovedEntitites = 0;
        final EntityManager entityManager = getEntityManager();
        EntityTransaction tx = null;
        try {
            tx = entityManager.getTransaction();
            if (!tx.isActive()) {
                tx.begin();
            }
            final List<String> jpqlQueryStringList = convert2DeleteStatement(specification, this.tableNameList);
            for (String jpqlQueryString : jpqlQueryStringList) {
                log.debug("JPQL query=" + jpqlQueryString);
                if (jpqlQueryString == null) {
                    throw new RepositoryException("JPQL query cannot be null [converted from specification=" + specification + "]");
                }
                final Query query = entityManager.createQuery(jpqlQueryString);
                numberOfRemovedEntitites += query.executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            log.error("Unable to perform remove operation with specification=" + specification, e);
        }
        return numberOfRemovedEntitites;
    }
}
