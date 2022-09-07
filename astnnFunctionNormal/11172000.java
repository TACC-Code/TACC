class BackupThread extends Thread {
    public MultiClockPipe(int dataPathSize, EntryMethod writeSide, EntryMethod readSide) {
        this(dataPathSize, writeSide.getClockPin(), readSide.getClockPin());
    }
}
