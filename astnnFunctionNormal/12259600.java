class BackupThread extends Thread {
    public static void routine(String auth, String remote, final String token) {
        TimeoutService.touch(token);
        String[] ss = remote.split("_");
        String host = ss[0];
        int port = Integer.parseInt(ss[1]);
        int connSize = Integer.parseInt(ss[2]);
        final InetSocketAddress address = new InetSocketAddress(host, port);
        ListSelector<Channel> selector = remoteChannelTable.get(token);
        if (null == selector) {
            selector = new ListSelector<Channel>();
            remoteChannelTable.put(token, selector);
        }
        final ListSelector<Channel> tmp = selector;
        for (int i = selector.size(); i < connSize; i++) {
            final RSocketAcceptedEvent ev = new RSocketAcceptedEvent();
            String[] sss = auth.split(":");
            ev.domain = sss[0];
            ev.port = 80;
            if (sss.length > 1) {
                ev.port = Integer.parseInt(sss[1]);
            }
            ChannelFuture future = newRemoteChannelFuture(address, token);
            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture f) throws Exception {
                    if (f.isSuccess()) {
                        tmp.add(f.getChannel());
                        Buffer content = new Buffer(16);
                        ev.encode(content);
                        ChannelBuffer msg = ChannelBuffers.wrappedBuffer(content.getRawBuffer(), content.getReadIndex(), content.readableBytes());
                        f.getChannel().write(msg);
                    } else {
                        closeRsocketChannel(token, f.getChannel());
                    }
                }
            });
        }
    }
}
