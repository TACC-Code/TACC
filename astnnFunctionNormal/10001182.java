class BackupThread extends Thread {
    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayPublicationId) {
        try {
            log.info("Eliminando ANUNCIOS: " + sArrayPublicationId);
            String sSql = Statements.DELETE_MASIVE_PUBLICATION;
            sSql = sSql.replaceFirst("v1", sArrayPublicationId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de publicacaciones eliminadas => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("publication.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("publication.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
}
