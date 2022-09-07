class BackupThread extends Thread {
    public Displayable getChannelScreen() {
        if (channel == null) {
            channel = new Thinlet(this);
            try {
                channel.add(main.parse(Constants.XML_CHANNEL));
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.addCommand(getRefreshCommand());
            channel.addCommand(getItemCommand());
            channel.addCommand(getBackCommand());
            channel.setCommandListener(this);
        }
        updateChannelScreen();
        return channel;
    }
}
