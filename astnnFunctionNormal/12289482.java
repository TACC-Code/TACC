class BackupThread extends Thread {
    public int executeUpdate(String cmd) throws RemoteException {
        try {
            Transaction tx = rconn.getTransaction();
            int result = SimpleDB.planner().executeUpdate(cmd, tx);
            rconn.commit();
            return result;
        } catch (RuntimeException e) {
            rconn.rollback();
            throw e;
        }
    }
}
