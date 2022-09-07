class BackupThread extends Thread {
    public void synchronizeUserGroup(Long nQuestionGroupId, String sArrayUserId) {
        List<MaUser> listUserGroup = null;
        int nNumUserGroup = 0;
        int nNumArrayUser = 0;
        String sSql = null;
        try {
            sSql = Statements.UPDATE_FLAG_Y_USER_QUESTION_GROUP;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            EntityManagerHelper.beginTransaction();
            Query query = EntityManagerHelper.createNativeQuery(sSql);
            query.setParameter(1, "Y");
            query.setParameter(2, nQuestionGroupId);
            int nDeleted = query.executeUpdate();
            sSql = Statements.UPDATE_FLAG_N_USER_QUESTION_GROUP;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            query = EntityManagerHelper.createNativeQuery(sSql);
            query.setParameter(1, "N");
            query.setParameter(2, nQuestionGroupId);
            nDeleted = query.executeUpdate();
            sSql = Statements.SELECT_MA_USER_IN;
            sSql = sSql.replaceFirst("v1", sArrayUserId);
            query = EntityManagerHelper.createNativeQuery(sSql, MaUser.class);
            query.setParameter(1, nQuestionGroupId);
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            listUserGroup = query.getResultList();
            nNumArrayUser = listUserGroup.size();
            for (Iterator iterator = listUserGroup.iterator(); iterator.hasNext(); ) {
                MaUser maUser = (MaUser) iterator.next();
                query = EntityManagerHelper.createNativeQuery(Statements.SELECT_CO_USER_QUESTION_GROUP_USER, CoUserQuestionGroup.class);
                query.setParameter(1, maUser.getUserId());
                query.setParameter(2, nQuestionGroupId);
                query.setHint(QueryHints.REFRESH, HintValues.TRUE);
                Vector vecResult = (Vector) query.getResultList();
                if (vecResult.size() == 0) {
                    CoUserQuestionGroupId CoUserQuestionGroupId = new CoUserQuestionGroupId(nQuestionGroupId, maUser.getUserId());
                    CoUserQuestionGroup coUserQuestionGroup = new CoUserQuestionGroup();
                    coUserQuestionGroup.setToQuestionGroup(new ToQuestionGroupDAO().findById(nQuestionGroupId));
                    coUserQuestionGroup.setMaUser(maUser);
                    coUserQuestionGroup.setId(CoUserQuestionGroupId);
                    coUserQuestionGroup.setFlagDeleted("N");
                    new CoUserQuestionGroupDAO().save(coUserQuestionGroup);
                } else {
                }
            }
            EntityManagerHelper.commit();
        } catch (Exception e) {
            log.info("Error buscando el estado para usuarios por grupo ");
            EntityManagerHelper.rollback();
        }
    }
}
