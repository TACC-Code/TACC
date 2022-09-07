class BackupThread extends Thread {
    public void cloneTest(Long nTestOldId, Long nTestNew) {
        CoTest coTestOld = new CoTestDAO().findById(nTestOldId);
        Set<CoQuestion> setQuestion = coTestOld.getCoQuestions();
        if (setQuestion.size() != 0) {
            for (Iterator<CoQuestion> iterator = setQuestion.iterator(); iterator.hasNext(); ) {
                EntityManagerHelper.beginTransaction();
                log.info("\n\n*****************************************");
                CoQuestion coQuestionOld = iterator.next();
                Long nQuestionNewId = getSequence("sq_co_question");
                Long nQuestionOldId = coQuestionOld.getQuestionId();
                Query query = EntityManagerHelper.createNativeQuery(Statements.CLONE_QUESTION);
                query.setParameter(1, nQuestionNewId);
                query.setParameter(2, coTestOld.getTestId());
                query.setParameter(3, nQuestionOldId);
                int nUpdate = query.executeUpdate();
                log.info("Clonaci�n NEWQUESTION[" + nQuestionNewId + "] = Modificados: " + nUpdate);
                EntityManagerHelper.commit();
                CoQuestion coQuestion = new CoQuestionDAO().findById(nQuestionNewId);
                EntityManagerHelper.refresh(coQuestion);
                if (coQuestion != null) {
                    EntityManagerHelper.beginTransaction();
                    query = EntityManagerHelper.createNativeQuery(Statements.CLONE_QUESTION_MATERIAL);
                    query.setParameter(1, nQuestionNewId);
                    query.setParameter(2, nQuestionOldId);
                    nUpdate = query.executeUpdate();
                    log.info("Clonaci�n[" + nQuestionOldId + "] - NEWQUESTION[" + nQuestionNewId + "]" + " - Materiales " + nUpdate);
                    EntityManagerHelper.commit();
                    new DataManagerQuestion().cloneQuestion(nQuestionOldId, nQuestionNewId);
                }
            }
        }
        EntityManagerHelper.beginTransaction();
        cloneUserHistory(nTestOldId, nTestNew);
        EntityManagerHelper.rollback();
    }
}
