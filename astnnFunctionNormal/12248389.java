class BackupThread extends Thread {
        public void releaseNonSync(ClientChannel clientChannel) {
            clientChannel.clearName();
            clientChannel.setSessionId(null);
            clientChannel.setOriginalChannelId(null);
            ChannelManager channelManager = Context.getInstance().getChannelManager();
            list.moveAfter(endIter, clientChannel.getIterInPool());
            if (size >= clientProps.getMinPoolSize()) {
                clientChannel.setTimeout(clientProps.getKeepAliveTime());
                clientChannel.reactivate();
            } else if (clientChannel.getTimeout() > 0) {
                size++;
                clientChannel.setIdle(true);
                free(clientChannel.getId());
            }
            channelManager.onReadRequired(clientChannel);
        }
}
