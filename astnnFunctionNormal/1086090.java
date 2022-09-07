class BackupThread extends Thread {
    public boolean open(IParallelContext pc) {
        boolean retval;
        if (_read_write) {
            retval = openWrite(pc);
        } else {
            retval = openRead(pc);
        }
        return retval;
    }
}
