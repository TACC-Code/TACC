class BackupThread extends Thread {
    private void channelLevelChanged() {
        if (currentCueIndex != -1) {
            int start = currentRange * levelControls.size();
            for (int i = 0; i < levelControls.size(); i++) {
                LevelControl levelControl = levelControls.get(i);
                int index = start + i;
                if (index < currentChannelIndexes.length) {
                    int channelIndex = currentChannelIndexes[index];
                    int old = getCues().getLightCues().getDetail(currentCueIndex).getChannelLevel(channelIndex).getChannelIntValue();
                    if (old != levelControl.getLevel()) {
                        float value = levelControl.getLevel() / 100f;
                        getCues().getLightCues().setChannel(currentCueIndex, channelIndex, value);
                    }
                }
            }
        }
    }
}
