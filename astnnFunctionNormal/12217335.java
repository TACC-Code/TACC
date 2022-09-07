class BackupThread extends Thread {
    protected static Db companyDbRW(Db mainDb, int companyId) throws Exception {
        DbAuth auth = getAuth(mainDb, companyId);
        DbPool pool = null;
        if (auth != null) pool = getPoolRW(auth);
        _logger.debug("Returning pooled read/write db object (" + (pool != null ? pool.getPoolName() : "null") + ") for company id (" + companyId + ") database.");
        return (pool == null ? null : pool.getDb());
    }
}
