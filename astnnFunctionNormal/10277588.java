class BackupThread extends Thread {
    public static void undoQueryContext(QueryContext queryContext) throws Exception {
        queryContext.getConnection().rollback();
        if (log.isNotice()) log.notice("rollbacked con (" + queryContext.getConnection() + "): " + queryContext.getChannel());
    }
}
