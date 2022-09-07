class BackupThread extends Thread {
    public ChannelModeForm(Shell parent, int style, Channel channel) {
        super(parent, style);
        setChannel(channel);
        setText(String.format("Mode for channel %s", getChannel().getName()));
    }
}
