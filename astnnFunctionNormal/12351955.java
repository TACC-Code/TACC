class BackupThread extends Thread {
    public void onChannelJoin(ServerEvent event) {
        Debug.println("ChatApp.onChannelJoin(" + event + ")");
        Channel chan = event.getChannel();
        ChannelFrame chatWin = new ChannelFrame(chan);
        _mdiPanel.addClientFrame(chatWin);
        _framesByPanel.put(chatWin.getChannelPanel(), chatWin);
    }
}
