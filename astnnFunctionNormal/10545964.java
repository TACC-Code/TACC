class BackupThread extends Thread {
    public void assertWritable() {
        if (readOnly) throw new IllegalStateException("This instance of OrganisationCf does not allow write!");
    }
}
