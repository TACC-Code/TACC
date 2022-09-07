class BackupThread extends Thread {
    public void writeDevice(byte[] state) throws OneWireIOException, OneWireException {
        if (adapter.select(address)) {
            byte[] writeblock = new byte[6];
            writeblock[0] = WRITE_CLOCK_COMMAND;
            System.arraycopy(state, 0, writeblock, 1, 5);
            adapter.dataBlock(writeblock, 0, 6);
            byte[] readblock = readDevice();
            if ((readblock[0] & 0x0C) != (state[0] & 0x0C)) throw new OneWireIOException("Failed to write to the clock register page");
            for (int i = 1; i < 5; i++) if (readblock[i] != state[i]) throw new OneWireIOException("Failed to write to the clock register page");
        } else throw new OneWireIOException("Device not found on one-wire network");
    }
}
