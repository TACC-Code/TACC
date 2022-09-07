class BackupThread extends Thread {
    private void deassignChannels() {
        if (currentRange >= 0 && currentCueIndex >= 0) {
            int start = currentRange * levelControls.size();
            int end = start + levelControls.size();
            if (end > currentChannelIndexes.length) {
                end = currentChannelIndexes.length;
            }
            LightCueDetail detail = getDetail(currentCueIndex);
            for (int i = start; i < end; i++) {
                int channelIndex = currentChannelIndexes[i];
                CueChannelLevel level = detail.getChannelLevel(channelIndex);
                level.setLevelControl(null);
            }
        }
        currentRange = -1;
        currentCueIndex = -1;
        currentChannelIndexes = new int[0];
    }
}
