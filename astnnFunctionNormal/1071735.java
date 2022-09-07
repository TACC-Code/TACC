class BackupThread extends Thread {
        public Iterator<MonitoredChannel> getChannels() {
            Set<MonitoredChannel> monitored = new HashSet<MonitoredChannel>();
            for (Iterator<WeakReference<SimpleChannel<?>>> iterator = channels.iterator(); iterator.hasNext(); ) {
                WeakReference<SimpleChannel<?>> ref = iterator.next();
                if (ref.get() != null) {
                    monitored.add(new MonitoredChannelImpl(ref.get()));
                }
            }
            return monitored.iterator();
        }
}
