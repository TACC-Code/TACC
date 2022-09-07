class BackupThread extends Thread {
    private void updateMainScreen() {
        Vector all = getChannelDAO().findAll();
        Object channels = main.find("channels");
        main.removeAll(channels);
        boolean select = true;
        for (int i = 0; i < all.size(); i++) {
            ChannelVO channel = (ChannelVO) all.elementAt(i);
            long id = channel.getId();
            Object channelsNode = main.create("item");
            main.setString(channelsNode, "text", channel.getName());
            if (select) {
                main.setBoolean(channelsNode, "selected", true);
                select = false;
            }
            main.putProperty(channelsNode, "id", new Integer(channel.getId()));
            main.add(channels, channelsNode);
        }
    }
}
