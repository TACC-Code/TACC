class BackupThread extends Thread {
    public static void commitQueryContext(QueryContext queryContext) throws Exception {
        queryContext.getConnection().commit();
        if (log.isNotice()) log.notice("committed con (" + queryContext.getConnection() + "): " + queryContext.getChannel());
    }
}
