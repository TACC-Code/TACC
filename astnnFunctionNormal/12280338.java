class BackupThread extends Thread {
    Family(Tester tester, int add, int rem, int read, int write, int max, Defr defr) throws AccessException {
        this(tester, add, rem, read, write, max, "Test", defr);
    }
}
