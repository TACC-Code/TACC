class BackupThread extends Thread {
    public Permission(boolean readAccess, boolean writeAccess) {
        this.readable = readAccess;
        this.writeable = (readAccess == true && writeAccess == true ? true : false);
    }
}
