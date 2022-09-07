class BackupThread extends Thread {
    private void trac258_AdjustVerticalToolbar(StateProp prop) {
        ArrayList channelList = (ArrayList) prop.getChannelProps().clone();
        prop.setChannelProps(channelList);
    }
}
