class BackupThread extends Thread {
    @Override
    public <T extends Serializable> Channel<T> getChannel(final String name, boolean datastored) {
        final LinkedList<T> buffer = new LinkedList<T>();
        final ChannelImpl<T> channel = new ChannelImpl<T>(api, buffer, name, flushTimeout, flushBucketSize, maxFlushRetries);
        new Poller<ArrayList<T>>(new Poller.Query<ArrayList<T>>() {

            @Override
            public void doQuery(final AsyncCallback<ArrayList<T>> callback) {
                api.readAll(name, new AsyncCallback<ArrayList<ChannelMessage<T>>>() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(ArrayList<ChannelMessage<T>> result) {
                        callback.onSuccess((ArrayList<T>) result);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                    }
                });
            }
        }, new Poller.Publish<ArrayList<T>>() {

            public void doPublish(ArrayList<T> result) {
                for (T e : result) if (channel.callback != null) {
                    channel.callback.onSuccess(e);
                } else {
                    buffer.add(e);
                }
            }

            ;
        }, Integer.MAX_VALUE).start();
        return channel;
    }
}
