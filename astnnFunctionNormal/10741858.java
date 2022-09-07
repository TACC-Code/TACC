class BackupThread extends Thread {
    @Override
    protected boolean delete(Long photoId) {
        String q = "delete from PhotoEn obj where obj.photoId=:photoId";
        EntityManager em = getEM();
        EntityTransaction tx = em.getTransaction();
        try {
            Query query = em.createQuery(q);
            query.setParameter("photoId", photoId);
            tx.begin();
            int result = query.executeUpdate();
            tx.commit();
            return (result >= 1);
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            em.close();
        }
    }
}
