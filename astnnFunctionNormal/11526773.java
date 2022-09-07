class BackupThread extends Thread {
    public int getChannelNumber() {
        return ((Integer) content.get(FIELD_CHANNEL)).intValue();
    }
}
