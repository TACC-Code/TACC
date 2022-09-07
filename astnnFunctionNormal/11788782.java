class BackupThread extends Thread {
    public void deletarLocador(final Locador... locadors) throws Exception {
        for (Locador locador : locadors) {
            Session session = Hibernate.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                String sqlDeletaLocador = "DELETE FROM locadorimovelpk WHERE codLocador = :id ;";
                SQLQuery createSQLQuery = session.createSQLQuery(sqlDeletaLocador);
                createSQLQuery.setInteger("id", locador.getCodLocador());
                System.out.println(createSQLQuery.executeUpdate());
                session.delete(locador);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException("HIBERNATE Erro no Deletar Locador: ", e);
            } catch (Exception e) {
                throw new Exception("GERAL Erro no Deletar Locador: ", e);
            }
        }
    }
}
