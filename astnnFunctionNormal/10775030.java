class BackupThread extends Thread {
    @Override
    protected AbstractIRCChannel getChannelImpl(String name) {
        return new JaicWainIRCChannel(name, this);
    }
}
