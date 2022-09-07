class BackupThread extends Thread {
    public static ChannelWrapper getInstance(final DataAdaptor adaptor, final Accelerator accelerator) {
        final ChannelSource channelSource = getChannelSource(adaptor, accelerator);
        if (channelSource != null) {
            final ChannelWrapper wrapper = new ChannelWrapper(channelSource);
            if (adaptor.hasAttribute("plotting")) {
                final boolean plotting = adaptor.booleanValue("plotting");
                wrapper.setPlotting(plotting);
            }
            if (adaptor.hasAttribute("enabled")) {
                final boolean enabled = adaptor.booleanValue("enabled");
                wrapper.setEnable(enabled);
                if (enabled && !adaptor.hasAttribute("plotting")) {
                    wrapper.setPlotting(true);
                }
            }
            return wrapper;
        } else {
            return null;
        }
    }
}
