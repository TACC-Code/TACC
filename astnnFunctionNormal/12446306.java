class BackupThread extends Thread {
    protected JComponent createEpgPane() {
        if (epgPane == null) {
            if (channels == null) getChannelData();
            getStatusBar().setMessage("EPG-Daten werden gelesen und aufbereitet ...");
            epg = getEpgData1();
            FilterList<ChannelInfo> fl = new FilterList<ChannelInfo>(channels, new FavoriteChannel());
            epgView = new EpgTableView(epg, getAppConfig().getMinEventLength());
            epgView.setTimerCreationHandler(timerHandler);
            epgView.setFavoriteChannels(fl);
            epgPane = new ApplicationPage(getName() + ".epg", epgView.getView());
        }
        return epgPane;
    }
}
