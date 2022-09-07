class BackupThread extends Thread {
    public void setDisplayNameSocrates(int channel, String displayName) {
        if (displayName == null) return;
        if (displayName.equals("")) return;
        ChannelDescriptor channelDescriptor = (ChannelDescriptor) ((PlatoBuffer) dpBuffer).getFormat();
        int fieldPos = channel + Constants.referenceFields.length;
        channelDescriptor.setFieldName(fieldPos, displayName);
        IPaneViewPart paneViewPart = (IPaneViewPart) viewPart;
        if (stid.compareTo(Constants.lineGraphStid) == 0) {
            LineProp prop = (LineProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setFieldName(displayName);
        } else if (stid.compareTo(Constants.stateGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setFieldName(displayName);
        } else if (stid.compareTo(Constants.discreteGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setFieldName(displayName);
        } else if (stid.compareTo(Constants.tableGraphStid) == 0) {
            TableProp prop = (TableProp) paneViewPart.getProperties();
            prop.setFieldName(displayName, channel + Constants.referenceFields.length);
        } else if (stid.compareTo(Constants.barGraphStid) == 0) {
            BarProp prop = (BarProp) paneViewPart.getProperties();
            ArrayList channelList = (ArrayList) prop.getValueProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            cp.setFieldName(displayName);
            prop.setValueProps(channelList);
            IPane iPane = paneViewPart.getViewInfo().getPaneInfo(0).getPane();
            ((BarPane) iPane).platoRefresh();
        }
        paneViewPart.updatePropertiesView();
        updateLegendLook();
    }
}
