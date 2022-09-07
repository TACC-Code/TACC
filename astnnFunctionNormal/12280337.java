class BackupThread extends Thread {
    Family(Tester tester, int add, int rem, int read, int write, int max, String name) throws AccessException {
        this(tester, add, rem, read, write, max, name, null);
    }
}
