class BackupThread extends Thread {
    public static void addChannel(String siteURL, String channelTags) throws LinkNotFoundException, MalformedURLException, SAXException, IOException {
        Channel newChannel = ChannelFactory.getChannelFromSite(siteURL);
        newChannel.setTags(parseTags(channelTags));
        channels.add(newChannel, ChannelFactory.getDownloadedItems());
        visibleChannels.add(newChannel);
        Collections.sort(visibleChannels, channelComparator);
        for (String tag : newChannel.getTags()) {
            if (!tags.contains(tag)) {
                tags.add(tag);
            }
        }
        Collections.sort(tags);
        channels.write();
    }
}
