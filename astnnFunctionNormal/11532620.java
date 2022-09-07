class BackupThread extends Thread {
    public void MEM() throws IrregularStringOfBitsException, MemoryElementNotFoundException, AddressErrorException, IrregularWriteOperationException {
        long address = TR[OFFSET_PLUS_BASE].getValue();
        Dinero din = Dinero.getInstance();
        din.Load(Converter.binToHex(Converter.positiveIntToBin(64, address)), 1);
        MemoryElement memEl = memory.getCell((int) address);
        TR[LMD_REGISTER].writeByte(memEl.readByte((int) (address % 8)));
        if (enableForwarding) {
            doWB();
        }
    }
}
