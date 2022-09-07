class BackupThread extends Thread {
    public void deleteUserRole(Integer userId, String[] roleIds) throws Exception {
        Transaction tx = null;
        try {
            tx = getSession().beginTransaction();
            String delStr = null;
            for (int i = 0; i < roleIds.length; i++) {
                delStr = "delete from " + AcUserRole.class.getName() + " where acUser.id = '" + userId + "' and acRole.id = '" + roleIds[i] + "'";
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
