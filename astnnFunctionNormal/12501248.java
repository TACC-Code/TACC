class BackupThread extends Thread {
    public void updateChannelInputs() {
        for (int i = 0; i < channelInputs.size(); i++) {
            float value = 0f;
            boolean found = false;
            for (int j = 0; !found && j < dimmers.size(); j++) {
                final int channelIndex = dimmers.get(j).getChannelId();
                if (channelIndex == i) {
                    found = true;
                    value = dimmers.get(j).getValue();
                }
            }
            channelInputs.get(i).setValue(value);
        }
    }
}
