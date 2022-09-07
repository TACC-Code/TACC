class BackupThread extends Thread {
    private synchronized boolean write_read_copy_quick(int secret_page, int secret_offset) throws OneWireIOException, OneWireException {
        int addr = (secret_page << 5) + secret_offset;
        byte[] buffer = byte_buffer;
        buffer[0] = (byte) RESUME;
        buffer[1] = (byte) WRITE_SCRATCHPAD;
        buffer[2] = (byte) addr;
        buffer[3] = (byte) (addr >> 8);
        int length = 32 - secret_offset;
        if (DEBUG) {
            IOHelper.writeLine("------------------------------------");
            IOHelper.writeLine("write_read_copy_quick");
            IOHelper.writeLine("address");
            IOHelper.writeBytesHex(address);
            IOHelper.writeLine("write scratchpad");
            IOHelper.write("target address: 0x");
            IOHelper.writeHex((byte) (addr >> 8));
            IOHelper.writeLineHex((byte) addr);
        }
        adapter.reset();
        System.arraycopy(FF, 0, buffer, 4, length + 2);
        adapter.dataBlock(buffer, 0, length + 6);
        if (CRC16.compute(buffer, 1, length + 5, 0) != 0xB001) {
            return false;
        }
        buffer[1] = (byte) READ_SCRATCHPAD;
        System.arraycopy(FF, 0, buffer, 2, 8);
        adapter.reset();
        adapter.dataBlock(buffer, 0, 5);
        if (DEBUG) {
            IOHelper.writeLine("read scratchpad");
            IOHelper.write("target address: 0x");
            IOHelper.writeHex((byte) buffer[3]);
            IOHelper.writeLineHex((byte) buffer[2]);
            IOHelper.write("ES: 0x");
            IOHelper.writeLineHex((byte) buffer[4]);
            IOHelper.writeLine("------------------------------------");
        }
        buffer[1] = COPY_SCRATCHPAD;
        adapter.reset();
        adapter.dataBlock(buffer, 0, 8);
        if (buffer[7] == (byte) 0x0ff) return waitForSuccessfulFinish(); else return true;
    }
}
