class BackupThread extends Thread {
    public void setValue(final int channelIndex, final float value) {
        for (int i = 0; i < size(); i++) {
            Dimmer dimmer = get(i);
            if (dimmer.getChannelId() == channelIndex) {
                dimmer.setValue(value);
            }
        }
        doNotMarkDirty();
    }
}
