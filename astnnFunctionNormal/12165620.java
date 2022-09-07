class BackupThread extends Thread {
    public MemorySection(int baseAddress, int length, boolean read, boolean write, boolean execute) {
        this.baseAddress = baseAddress & Memory.addressMask;
        this.length = length;
        this.read = read;
        this.write = write;
        this.execute = execute;
    }
}
