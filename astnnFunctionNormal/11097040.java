class BackupThread extends Thread {
    public void removeProduct(Product product) throws PersistenceException {
        logger.info("Removing product...");
        if (product == null) {
            String error = "Product is null.";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("delete from Product p where " + " p = :product ");
            query.setParameter("product", product);
            query.executeUpdate();
            em.getTransaction().commit();
        } catch (Exception ex) {
            String error = "Error removing product: " + ex.getMessage();
            logger.error(error);
            em.getTransaction().rollback();
            throw new PersistenceException(ex);
        } finally {
            em.close();
        }
        logger.info("Product removed successfully.");
    }
}
