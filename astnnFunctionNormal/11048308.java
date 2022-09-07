class BackupThread extends Thread {
    public Backyard(HttpServletRequest req, HttpServletResponse resp) {
        this.resp = resp;
        this.req = req;
        if (!alternativ_impl) in = Guice.createInjector(new BackyardModule());
        channelhandler = in.getInstance(ChannelHandler.class);
        memberhandler = in.getInstance(MemberHandler.class);
        for (ChannelListenerBuffer key : channelListenerBuffer) {
            if (key.getChannel().matches(".*_nb")) {
                addChannelListener(new Integer(key.getChannel().split("_")[0]), key.getCl());
            } else {
                addChannelListener(key.getChannel(), key.getCl());
            }
        }
        channelListenerBuffer.clear();
        this.member = memberhandler.getMember(req.getSession().getId());
        listenToChannel(0);
    }
}
