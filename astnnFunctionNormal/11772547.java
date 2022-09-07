class BackupThread extends Thread {
    public RestServiceResult delete(RestServiceResult serviceResult, CoTest coTest) {
        try {
            log.info("Eliminando la prueba: " + coTest.getTestName());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_TEST);
            query.setParameter(1, coTest.getTestId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { coTest.getTestName() };
            log.info("Prueba eliminada con �xito: " + coTest.getTestName());
            serviceResult.setMessage(MessageFormat.format(bundle.getString("test.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al eliminar la prueba: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { coTest.getTestName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("test.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
}
