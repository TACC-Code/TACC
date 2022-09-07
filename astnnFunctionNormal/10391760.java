class BackupThread extends Thread {
    public RestServiceResult delete(RestServiceResult serviceResult, ToQuestionGroup toQuestionGroup) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_TO_QUESTION_GROUP);
            query.setParameter(1, toQuestionGroup.getExerciseGroupId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            EntityManagerHelper.refresh(toQuestionGroup);
            Object[] arrayParam = { toQuestionGroup.getGroupName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("questionGroup.delete.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el ejercicio grupal Pregunta: " + e.getMessage());
            serviceResult.setError(true);
            Object[] arrayParam = { toQuestionGroup.getGroupName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("questionGroup.delete.error") + e.getMessage(), arrayParam));
        }
        return serviceResult;
    }
}
