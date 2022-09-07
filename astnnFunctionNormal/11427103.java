class BackupThread extends Thread {
    public void deleteProductPackageItemFrom(Integer id) throws Exception {
        Transaction tx = null;
        String hql = "delete from " + ProductPackageItem.class.getName() + " where productPackage.id = ?";
        try {
            tx = getSession().beginTransaction();
            Query queryObject = getSession().createQuery(hql);
            queryObject.setParameter(0, id);
            queryObject.executeUpdate();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        }
    }
}
