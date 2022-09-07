class BackupThread extends Thread {
    public void deleteBoss(Integer userId, String[] bossIds) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String delStr = null;
            for (int i = 0; i < bossIds.length; i++) {
                delStr = "delete from " + AcUserRelation.class.getName() + " where acUserByStaffId.id = '" + userId + "' and acUserByBossId.id = '" + bossIds[i] + "'";
                Query queryObject = getSession().createQuery(delStr);
                queryObject.executeUpdate();
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
}
