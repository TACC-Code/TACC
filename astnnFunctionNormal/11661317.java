class BackupThread extends Thread {
    public void setMixer(Mixer mixer) {
        this.mixer = null;
        enabled.setSelected(mixer.isEnabled());
        extraRenderText.setEnabled(mixer.isEnabled());
        extraRenderText.setText(Float.toString(mixer.getExtraRenderTime()));
        channelsPanel.setChannelList(mixer.getChannels(), mixer.getSubChannels());
        subChannelsPanel.setChannelList(mixer.getSubChannels());
        masterPanel.clear();
        masterPanel.setChannel(mixer.getMaster());
        this.mixer = mixer;
        EffectEditorManager.getInstance().clear();
        SendEditorManager.getInstance().clear();
    }
}
