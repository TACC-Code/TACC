class BackupThread extends Thread {
        void write(RVMThread t, int depth, int slot, long value) {
            this.value = value;
            start(t, depth, slot);
        }
}
