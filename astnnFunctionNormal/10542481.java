class BackupThread extends Thread {
    public void setFocusChannel(final int cueIndex, final int rowIndex, final int[] channelIndexes) {
        StringBuilder b = new StringBuilder();
        b.append("channelIndexes: ");
        for (int i = 0; i < channelIndexes.length; i++) {
            b.append(i);
            b.append("/");
            b.append(channelIndexes[i]);
            b.append(" ");
        }
        Util.debug(b.toString());
        if (focus != FOCUS_CHANNEL) {
            deassign();
        }
        focus = FOCUS_CHANNEL;
        int newRange = rowIndex / levelControls.size();
        if (currentRange != newRange || currentCueIndex != cueIndex) {
            deassignChannels();
            currentRange = newRange;
            currentCueIndex = cueIndex;
            currentChannelIndexes = channelIndexes;
            int start = currentRange * levelControls.size();
            LightCueDetail detail = getDetail(currentCueIndex);
            for (int i = 0; i < levelControls.size(); i++) {
                LevelControl levelControl = levelControls.get(i);
                int index = start + i;
                if (index < channelIndexes.length) {
                    int channelIndex = channelIndexes[index];
                    CueChannelLevel level = detail.getChannelLevel(channelIndex);
                    level.setLevelControl(levelControl);
                    levelControl.getLevelHolder().setValue(level.getChannelIntValue(), levelControlListener);
                } else {
                    levelControl.setLevel(0);
                }
            }
        }
    }
}
