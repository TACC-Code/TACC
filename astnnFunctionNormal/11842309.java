class BackupThread extends Thread {
    protected DataQuery doCopyBetweenWindowsQuery(DataQuery request) throws NamingException {
        unthreadedCopyBetweenWindows(request.oldDN(), request.getExternalDataSource(), request.requestDN(), request.overwriteExistingData());
        request.setStatus(true);
        return finish(request);
    }
}
