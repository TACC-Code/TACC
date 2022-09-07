class BackupThread extends Thread {
    public RestServiceResult delete(RestServiceResult serviceResult, CoMatrixExercises1 coMatrixExercises1) {
        String sExerciseName = null;
        try {
            sExerciseName = coMatrixExercises1.getCoExercises1().getExerciseName();
            log.error("Eliminando la matriz tipo 1: " + sExerciseName);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_MATRIX_EXERCISES1);
            query.setParameter(1, coMatrixExercises1.getMatrixId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sExerciseName };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("matrixExercises1.delete.success"), arrayParam));
            log.info("Eliminando matriz tipo 1: " + sExerciseName);
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar la matriz tipo 1: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coMatrixExercises1.getCoExercises1().getExerciseName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("matrixExercises1.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
}
