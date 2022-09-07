class BackupThread extends Thread {
    protected boolean setIsValid() {
        boolean retval = _read_write && !_opened_before;
        return retval;
    }
}
