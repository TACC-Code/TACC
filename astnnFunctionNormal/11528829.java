class BackupThread extends Thread {
    public void addAgAnnouncementUser(AgAnnouncement agAnnouncement, Long nUserId) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.INSERT_AG_ANNOUNCEMENT_USER);
            query.setParameter(1, agAnnouncement.getAnnouncementId());
            query.setParameter(2, nUserId);
            query.executeUpdate();
            EntityManagerHelper.commit();
        } catch (Exception e) {
            EntityManagerHelper.rollback();
            log.info("Error al asociarle al anuncio un usuario.." + e.getStackTrace());
        }
    }
}
