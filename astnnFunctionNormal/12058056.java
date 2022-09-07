class BackupThread extends Thread {
    private void setRowPermsForOTHER(RowPermissions rowperms, boolean read, boolean write, boolean admin) throws DBException {
        rowperms.canOthersRead(read);
        rowperms.canOthersWrite(write);
        rowperms.canOthersAdministrate(admin);
        rowperms.addOrUpdate();
    }
}
