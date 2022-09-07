class BackupThread extends Thread {
    public void updateClient(Client client) throws OldVersionException, MyHibernateException {
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (int i = 0; i < client.getPhones().size(); i++) {
                if (client.getPhones().get(i).getNumber().equals("")) {
                    SQLQuery query = session.createSQLQuery("DELETE FROM phones WHERE id = " + client.getPhones().get(i).getId());
                    query.executeUpdate();
                    client.getPhones().remove(i);
                    i--;
                }
            }
            clientDAO.update(session, client);
            transaction.commit();
        } catch (RuntimeException ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            if (ex.getCause() instanceof SQLException) {
                SQLException sqle = (SQLException) ex.getCause();
                switch(sqle.getErrorCode()) {
                    case 1062:
                        throw new MyHibernateException(SQLErrorManager.extractKey(TYPE, sqle.getMessage()));
                }
            } else if (ex instanceof StaleObjectStateException) {
                throw new OldVersionException(ex);
            } else if (ex instanceof HibernateException) {
                throw new MyHibernateException((HibernateException) ex);
            }
            throw ex;
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
