class BackupThread extends Thread {
    protected static Db npCompanyDbRW(Db mainDb, int companyId) throws Exception {
        _logger.debug("Returning non-pooled read/write db object for company id (" + companyId + ") database.");
        DbAuth auth = getAuth(mainDb, companyId);
        if (auth == null) {
            return null;
        } else {
            return new Db(auth, null, DbConnection.getExternalDatabase(auth.getUsername(), auth.getPassword(), "org.postgresql.Driver", auth.getConnection()), false, true);
        }
    }
}
