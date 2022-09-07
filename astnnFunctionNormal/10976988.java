class BackupThread extends Thread {
    public static void selectTag(String tag) {
        tag = tag.trim();
        visibleChannels = new ArrayList<Channel>();
        if (tag.equals("all")) {
            visibleChannels = channels.getChannels();
        } else if (tag.equals("") || tag == null) {
            for (Channel channel : channels.getChannels()) {
                if (channel.getTags().size() == 0) {
                    visibleChannels.add(channel);
                }
            }
        } else {
            for (Channel channel : channels.getChannels()) {
                if (channel.containsTag(tag)) {
                    visibleChannels.add(channel);
                }
            }
        }
        Collections.sort(visibleChannels, channelComparator);
    }
}
