class BackupThread extends Thread {
        void write(RVMThread t, int depth, int slot, Object value) {
            this.value = value;
            start(t, depth, slot);
        }
}
