class BackupThread extends Thread {
    public void setInputValue(final int index, final float value) {
        if (index < inputs.size()) {
            inputs.get(index).setValue(value);
            final int channelIndex = dimmers.get(index).getChannelId();
            if (channelIndex != -1) {
                channelInputs.get(channelIndex).setValue(value);
            }
        }
    }
}
