class BackupThread extends Thread {
    public String getDisplayNameSocrates(int channel) {
        String result = null;
        IPaneViewPart paneViewPart = (IPaneViewPart) viewPart;
        if (stid.compareTo(Constants.lineGraphStid) == 0) {
            LineProp prop = (LineProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            result = cp.getFieldName();
        } else if (stid.compareTo(Constants.stateGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            result = cp.getFieldName();
        } else if (stid.compareTo(Constants.discreteGraphStid) == 0) {
            StateProp prop = (StateProp) paneViewPart.getProperties();
            ArrayList channelList = prop.getChannelProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            result = cp.getFieldName();
        } else if (stid.compareTo(Constants.tableGraphStid) == 0) {
            TableProp prop = (TableProp) paneViewPart.getProperties();
            result = prop.getFieldName(channel + Constants.referenceFields.length);
        } else if (stid.compareTo(Constants.barGraphStid) == 0) {
            BarProp prop = (BarProp) paneViewPart.getProperties();
            ArrayList channelList = (ArrayList) prop.getValueProps();
            ChannelProp cp = (ChannelProp) channelList.get(channel);
            result = cp.getFieldName();
        }
        return result;
    }
}
