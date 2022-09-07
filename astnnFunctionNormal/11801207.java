class BackupThread extends Thread {
    public RestServiceResult delete(RestServiceResult serviceResult, ToHandbook toHandbook) {
        try {
            log.info("Eliminando anotaci�n libreta: " + toHandbook.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_HANDBOOK);
            query.setParameter(1, toHandbook.getHandbookId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toHandbook);
            Object[] arrayParam = { toHandbook.getTitle() };
            log.info("Libreta eliminado con �xito: " + toHandbook.getTitle());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("handbook.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el Libreta: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toHandbook.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("handbook.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
}
