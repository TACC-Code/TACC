class BackupThread extends Thread {
    public void wideCopySelection(AChannelSelection orig) {
        for (int i = 0; i < getNumberOfChannels(); i++) {
            AChannel ch = getChannel(i);
            ((AChannelSelection) ch.getSelection()).copy(orig);
        }
    }
}
