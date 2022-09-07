class BackupThread extends Thread {
    public int getChannel() {
        this.getData(CHANNEL_REG, buffer, 1);
        return buffer[0];
    }
}
