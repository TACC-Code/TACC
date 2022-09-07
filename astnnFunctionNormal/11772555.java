class BackupThread extends Thread {
    public RestServiceResult createQuestionWeighted(RestServiceResult serviceResult, String sArrayQuestionWeightedId, CoTest coTest) {
        try {
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(Statements.DELETE_CO_TEST_QUESTION_WEIGHTED);
            query.setParameter(1, coTest.getTestId());
            query.executeUpdate();
            StringTokenizer stringTokenizer = new StringTokenizer(sArrayQuestionWeightedId, ",");
            while (stringTokenizer.hasMoreTokens()) {
                CoQuestionWeighted coQuestionWeighted = new CoQuestionWeighted();
                StringTokenizer stringTokenizer2 = new StringTokenizer(stringTokenizer.nextToken(), "|");
                String temp = stringTokenizer2.nextToken();
                if (temp.equals("null")) return serviceResult;
                long nQuestionNum = Long.parseLong(temp);
                double nWeighted = Double.parseDouble(stringTokenizer2.nextToken());
                coQuestionWeighted.setQuestionWeightedId(getSequence("sq_co_menu"));
                coQuestionWeighted.setQuestionNum(nQuestionNum);
                coQuestionWeighted.setWeighted(nWeighted);
                coQuestionWeighted.setCoTest(coTest);
                new CoQuestionWeightedDAO().save(coQuestionWeighted);
            }
            EntityManagerHelper.commit();
            Object[] arrayParam = { coTest.getTestName() };
            serviceResult.setMessage(MessageFormat.format(bundle.getString("test.create.success"), arrayParam));
        } catch (PersistenceException e) {
            EntityManagerHelper.rollback();
            log.error("Error al guardar la asociaci�n - Pregunta- Material: " + e.getMessage());
            serviceResult.setError(true);
            serviceResult.setMessage(MessageFormat.format(bundle.getString("test.create.error"), e.getMessage()));
            Util.printStackTrace(log, e.getStackTrace());
        }
        return serviceResult;
    }
}
