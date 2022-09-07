class BackupThread extends Thread {
    private static ChannelSource getChannelSource(final DataAdaptor adaptor, final Accelerator accelerator) {
        if (adaptor.hasAttribute("pv")) {
            final String pv = adaptor.stringValue("pv");
            if (pv != null) {
                return new SimpleChannelSource(pv);
            } else {
                return null;
            }
        } else if (adaptor.hasAttribute("channelRef")) {
            final String channelRefID = adaptor.stringValue("channelRef");
            return new NodeChannelSource(accelerator, channelRefID);
        } else {
            return null;
        }
    }
}
