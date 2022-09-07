class BackupThread extends Thread {
    public static void removeFacesUpdatedBefore(Date lastUpdateDate) {
        Transaction aTransaction = null;
        try {
            Session aSession = PXObjectStore.getInstance().getThreadSession();
            aTransaction = aSession.beginTransaction();
            String queryString = "delete org.pixory.pxmodel.PXAlbumFace album where album.lastUpdateDate < :lastUpdateDate";
            aSession.createQuery(queryString).setTimestamp("lastUpdateDate", lastUpdateDate).executeUpdate();
            aTransaction.commit();
        } catch (Exception anException) {
            if (aTransaction != null) {
                try {
                    aTransaction.rollback();
                } catch (Exception e) {
                }
            }
            LOG.warn(null, anException);
        }
    }
}
