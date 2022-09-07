class BackupThread extends Thread {
    Family(Tester tester, int add, int rem, int read, int write, int max, String name, Defr defr) throws AccessException {
        this.tester = tester;
        member = new CommonTable[max];
        this.addIndex = add;
        this.remIndex = rem;
        this.readIndex = read;
        this.writeIndex = write;
        populate(0, max, name, defr);
    }
}
