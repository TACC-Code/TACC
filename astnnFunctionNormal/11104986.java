class BackupThread extends Thread {
    protected void loadChannels(int selectedChannelId) {
        List tgChannelsData = new ArrayList();
        List tgChannelsAvailable = getSongManager().getChannels();
        Combo tgChannelsCombo = this.instrumentCombo;
        tgChannelsCombo.removeAll();
        tgChannelsCombo.add(TuxGuitar.getProperty("track.instrument.default-select-option"));
        tgChannelsCombo.select(0);
        tgChannelsData.add(new Integer(-1));
        for (int i = 0; i < tgChannelsAvailable.size(); i++) {
            TGChannel tgChannel = (TGChannel) tgChannelsAvailable.get(i);
            tgChannelsData.add(new Integer(tgChannel.getChannelId()));
            tgChannelsCombo.add(tgChannel.getName());
            if (tgChannel.getChannelId() == selectedChannelId) {
                tgChannelsCombo.select(tgChannelsCombo.getItemCount() - 1);
            }
        }
        tgChannelsCombo.setData(tgChannelsData);
    }
}
