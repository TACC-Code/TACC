class BackupThread extends Thread {
    public MultiClockPipe(int dataPathSize, ClockPin writeClock, ClockPin readClock) {
        switch(dataPathSize) {
            case 1:
            case 8:
            case 16:
            case 32:
            case 64:
                break;
            default:
                throw new net.sf.openforge.forge.api.ForgeApiException("Illegal data path size to MultiClockPipe: " + dataPathSize + ", expected 1, 8, 16, 32, or 64");
        }
        if (writeClock == null) {
            writeClock = ClockDomain.GLOBAL.getClockPin();
        }
        if (readClock == null) {
            readClock = ClockDomain.GLOBAL.getClockPin();
        }
        moduleName = IPCORENAME + instanceCount;
        instanceCount++;
        asyncFifo = new IPCore(moduleName);
        asyncFifo.connectClock(readClock, "read_clock_in");
        asyncFifo.connectClock(writeClock, "write_clock_in");
        asyncFifo.connectReset(readClock.getResetPin(), "read_reset_in");
        asyncFifo.connectReset(writeClock.getResetPin(), "write_reset_in");
        asyncFifo.setWriter(this);
        writeEnable = new PinOut(asyncFifo, "write_enable_in", 1, 0);
        writeData = new PinOut(asyncFifo, "write_data_in", dataPathSize);
        fifoFull = new PinIn(asyncFifo, "full_out", 1);
        writeStatus = new PinIn(asyncFifo, "write_status_out", 4);
        readEnable = new PinOut(asyncFifo, "read_enable_in", 1, 0);
        readData = new PinIn(asyncFifo, "read_data_out", dataPathSize);
        fifoEmpty = new PinIn(asyncFifo, "empty_out", 1);
        readStatus = new PinIn(asyncFifo, "read_status_out", 4);
        this.bufferDepth = 511;
    }
}
