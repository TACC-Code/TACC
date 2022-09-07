class BackupThread extends Thread {
    public void loadValue() {
        double beatGear = ChannelFrame.channelGridPanel.channels[channelID].getChannelBeat().getGear();
        if (beatGear == 0.015625) b1.setSelected(true);
        if (beatGear == 0.03125) b2.setSelected(true);
        if (beatGear == 0.0625) b3.setSelected(true);
        if (beatGear == 0.125) b4.setSelected(true);
        if (beatGear == 0.25) b5.setSelected(true);
        if (beatGear == 0.5) b6.setSelected(true);
        if (beatGear == 1.0) b7.setSelected(true);
        if (beatGear == 2.0) b8.setSelected(true);
        if (beatGear == 4.0) b9.setSelected(true);
        if (beatGear == 8.0) b10.setSelected(true);
        if (beatGear == 16.0) b11.setSelected(true);
        if (beatGear == 32.0) b12.setSelected(true);
        gearCheckFromButtons();
    }
}
