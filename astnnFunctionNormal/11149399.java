class BackupThread extends Thread {
    protected int getChannelIndex(RGBBase.Channel ch) {
        int index = ch.getArrayIndex();
        index = ch == RGBBase.Channel.W ? 3 : index;
        return index;
    }
}
