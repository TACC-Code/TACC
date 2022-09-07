class BackupThread extends Thread {
    public synchronized int getChannel(int tunerNumber) {
        return tunedChannels[tunerNumber].getNumber();
    }
}
