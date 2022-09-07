class BackupThread extends Thread {
    public void setColorSocrates(int channel, RGB color) {
        IPaneViewPart paneViewPart = (IPaneViewPart) viewPart;
        if (stid.compareTo(Constants.lineGraphStid) == 0) {
            LineProp prop = (LineProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setForegroundRGB(color);
            IPane iPane = paneViewPart.getViewInfo().getPaneInfo(0).getPane();
            ((LinePane) iPane).platoRefresh();
        } else if (stid.compareTo(Constants.stateGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setForegroundRGB(color);
        } else if (stid.compareTo(Constants.discreteGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setForegroundRGB(color);
        } else if (stid.compareTo(Constants.tableGraphStid) == 0) {
        } else if (stid.compareTo(Constants.barGraphStid) == 0) {
            BarProp prop = (BarProp) paneViewPart.getProperties();
            ArrayList channelList = (ArrayList) prop.getValueProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setForegroundRGB(color);
            prop.setValueProps(channelList);
            IPane iPane = paneViewPart.getViewInfo().getPaneInfo(0).getPane();
            ((BarPane) iPane).platoRefresh();
        }
        paneViewPart.updatePropertiesView();
    }
}
