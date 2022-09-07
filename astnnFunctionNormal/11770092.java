class BackupThread extends Thread {
    public RestServiceResult delete(RestServiceResult serviceResult, CoParagraphCheckList coParagraphCheckList) {
        String sTitle = null;
        try {
            sTitle = coParagraphCheckList.getTitle();
            log.error("Eliminando la lista de chequeo: " + coParagraphCheckList.getTitle());
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.getEntityManager().createNativeQuery(Statements.DELETE_CHECK_LIST_STUDENT);
            query.setParameter(1, coParagraphCheckList.getCheckListId());
            query.executeUpdate();
            EntityManagerHelper.commit();
            Object[] arrayParam = { sTitle };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkListStudent.delete.success"), arrayParam));
            log.info("Eliminando el curso: " + coParagraphCheckList.getTitle());
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al actualizar el curso: " + e.getMessage());
            serviceResult.setError(true);
            Object[] args = { coParagraphCheckList.getTitle() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("checkListStudent.delete.error") + e.getMessage(), args));
        }
        return serviceResult;
    }
}
