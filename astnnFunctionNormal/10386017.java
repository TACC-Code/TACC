class BackupThread extends Thread {
    void updatePermissions() {
        boolean canRead = this._mappedLinkFileObject.canRead();
        boolean canWrite = this._mappedLinkFileObject.canWrite();
        if (canRead && canWrite) {
            this._permissionsString = "read/write";
        }
        if (canRead && !canWrite) {
            this._permissionsString = "read-only";
        }
        if (!canRead && canWrite) {
            this._permissionsString = "write-only";
        }
        if (!canRead && !canWrite) {
            this._permissionsString = "no access";
        }
    }
}
