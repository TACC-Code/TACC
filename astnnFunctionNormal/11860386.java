class BackupThread extends Thread {
    public String print() {
        return print(digest.digest());
    }
}
