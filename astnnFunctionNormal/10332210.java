class BackupThread extends Thread {
    public TmpDirNotWriteableReadableException(String msg) {
        super(String.format(Bundles.subgetBundle.getString("Cannot_read/write_to_temporary_directory_(%s),_aborting."), msg));
    }
}
