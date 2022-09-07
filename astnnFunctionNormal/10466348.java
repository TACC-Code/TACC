class BackupThread extends Thread {
    public void wideCopyMask(AChannelMask orig) {
        for (int i = 0; i < getNumberOfChannels(); i++) {
            AChannel ch = getChannel(i);
            ((AChannelMask) ch.getMask()).copy(orig);
        }
    }
}
