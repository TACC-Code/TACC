class BackupThread extends Thread {
    private void copy_table() {
        int first = getUnsignedValue(0);
        int second = getUnsignedValue(1);
        int size = getValue(2);
        MemoryAccess memaccess = getMemoryAccess();
        if (second == 0) {
            size = Math.abs(size);
            for (int i = 0; i < size; i++) {
                memaccess.writeByte(first + i, (byte) 0);
            }
        } else {
            if (size < 0 || first > second) {
                size = Math.abs(size);
                for (int i = 0; i < size; i++) {
                    memaccess.writeByte(second + i, memaccess.readByte(first + i));
                }
            } else {
                size = Math.abs(size);
                for (int i = size - 1; i >= 0; i--) {
                    memaccess.writeByte(second + i, memaccess.readByte(first + i));
                }
            }
        }
        nextInstruction();
    }
}
