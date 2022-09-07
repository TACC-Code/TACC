class BackupThread extends Thread {
    public SaveFileExistsException(String filename) {
        super("The file " + filename + " already exists.  Would you like to overwrite it?");
    }
}
