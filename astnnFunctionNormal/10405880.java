class BackupThread extends Thread {
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        Session session = null;
        try {
            if (!definition.isReadOnly()) {
                logger.debug("Creating managed TopLink Session with active UnitOfWork for read-write transaction");
                session = getSessionFactory().createManagedClientSession();
            } else {
                logger.debug("Creating plain TopLink Session without active UnitOfWork for read-only transaction");
                session = getSessionFactory().createSession();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Opened new session [" + session + "] for TopLink transaction");
            }
            TopLinkTransactionObject txObject = (TopLinkTransactionObject) transaction;
            txObject.setSessionHolder(new SessionHolder(session));
            txObject.getSessionHolder().setSynchronizedWithTransaction(true);
            switch(definition.getIsolationLevel()) {
                case TransactionDefinition.ISOLATION_READ_UNCOMMITTED:
                    break;
                case TransactionDefinition.ISOLATION_REPEATABLE_READ:
                    break;
                case TransactionDefinition.ISOLATION_SERIALIZABLE:
                    break;
            }
            if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
                txObject.getSessionHolder().setTimeoutInSeconds(definition.getTimeout());
            }
            if (!definition.isReadOnly() && !isLazyDatabaseTransaction()) {
                session.getActiveUnitOfWork().beginEarlyTransaction();
            }
            if (getDataSource() != null) {
                Connection con = getJdbcConnection(session);
                if (con != null) {
                    ConnectionHolder conHolder = new ConnectionHolder(con);
                    if (definition.getTimeout() != TransactionDefinition.TIMEOUT_DEFAULT) {
                        conHolder.setTimeoutInSeconds(definition.getTimeout());
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("Exposing TopLink transaction as JDBC transaction [" + con + "]");
                    }
                    TransactionSynchronizationManager.bindResource(getDataSource(), conHolder);
                    txObject.setConnectionHolder(conHolder);
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Not exposing TopLink transaction [" + session + "] as JDBC transaction because no JDBC Connection could be retrieved from it");
                    }
                }
            }
            TransactionSynchronizationManager.bindResource(getSessionFactory(), txObject.getSessionHolder());
        } catch (Exception ex) {
            SessionFactoryUtils.releaseSession(session, getSessionFactory());
            throw new CannotCreateTransactionException("Could not open TopLink Session for transaction", ex);
        }
    }
}
