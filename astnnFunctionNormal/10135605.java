class BackupThread extends Thread {
    private void setDelay(int index, boolean arm, String direction) {
        ChannelFrame.channelGridPanel.channels[channelID].getChannelBeat().setGear(gearValue[index], arm);
        gearButton[index].setSelected(true);
        if (ChannelFrame.channelGridPanel.channels[channelID].getChannelType().equals("clip")) {
            if (ChannelFrame.channelGridPanel.channels[channelID].getStretchMode().equals("discrete") && index > tightBPC_CutOff) {
                if (!ChannelFrame.controlPanel.clipParametersPanel.tightCheck.isSelected()) {
                    ChannelFrame.controlPanel.clipParametersPanel.tightCheck.doClick();
                }
            }
            if (ChannelFrame.channelGridPanel.channels[channelID].getStretchMode().equals("discrete") && index <= tightBPC_CutOff) {
                if (ChannelFrame.controlPanel.clipParametersPanel.tightCheck.isSelected()) {
                    ChannelFrame.controlPanel.clipParametersPanel.tightCheck.doClick();
                }
            }
            if (index == tightBPC_CutOff && direction.equals("up")) {
                index++;
                setDelay(index, arm, direction);
            }
            if (index == tightBPC_CutOff && direction.equals("down")) {
                index--;
                setDelay(index, arm, direction);
            }
        }
    }
}
