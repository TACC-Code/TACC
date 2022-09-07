class BackupThread extends Thread {
    public boolean isMaskEnabled() {
        for (int i = 0; i < getNumberOfChannels(); i++) {
            if (getChannel(i).isMaskEnabled()) {
                return true;
            }
        }
        return false;
    }
}
