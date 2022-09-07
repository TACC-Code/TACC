class BackupThread extends Thread {
    public String getCacheConcurrencyStrategy() {
        return "read-write";
    }
}
