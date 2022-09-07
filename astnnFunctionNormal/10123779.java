class BackupThread extends Thread {
        private void clockInBit() {
            if (bitsRead < 7) {
                address <<= 1;
                address |= inputPin ? 0x1 : 0x0;
            } else if (bitsRead == 7) {
                writeCommand = inputPin;
                if (!writeCommand) {
                    readData = registers[address].value;
                }
            } else if (bitsRead < 16) {
                if (writeCommand) {
                    inputWriteBit();
                } else {
                    outputReadBit();
                }
            }
            bitsRead++;
            if (bitsRead == 16) {
                if (writeCommand) {
                    registers[address].write((byte) writeValue);
                    if (readerPrinter != null) readerPrinter.println("CC1000.Reg[" + StringUtil.toHex(address, 2) + "] <= " + StringUtil.toMultirepString(writeValue, 8));
                } else {
                    if (readerPrinter != null) readerPrinter.println("CC1000.Reg[" + StringUtil.toHex(address, 2) + "] -> " + StringUtil.toMultirepString(readData, 8));
                }
                bitsRead = 0;
                address = 0;
            }
        }
}
