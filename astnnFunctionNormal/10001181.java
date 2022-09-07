class BackupThread extends Thread {
    public RestServiceResult delete(RestServiceResult serviceResult, ToPublication toPublication) {
        try {
            log.info("Eliminando la publicacion: " + toPublication.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_PUBLICATION);
            query.setParameter(1, toPublication.getPublicationId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toPublication);
            Object[] arrayParam = { toPublication.getTitle() };
            log.info("Publicacion eliminada con �xito: " + toPublication.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("publication.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la publicacion: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toPublication.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("publication.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
}
