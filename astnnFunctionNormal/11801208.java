class BackupThread extends Thread {
    public RestServiceResult deleteMasive(RestServiceResult serviceResult, String sArrayHandbookId) {
        try {
            log.info("Eliminando DIARIO: " + sArrayHandbookId);
            String sSql = Statements.DELETE_MASIVE_HANDBOOK;
            sSql = sSql.replaceFirst("v1", sArrayHandbookId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            int nDeleted = query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { nDeleted };
            log.info(" N�mero de ANOTACI�N eliminados => " + nDeleted);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("handbook.delete.success"), arrayParam));
        } catch (Exception e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar el diario: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(bundle.getString("handbook.delete.error") + e.getMessage());
        }
        return serviceResult;
    }
}
