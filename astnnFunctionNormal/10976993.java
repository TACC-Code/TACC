class BackupThread extends Thread {
    public static int importChannelList(String fileLocation) throws IOException, SAXException {
        List<Channel> importedChannels = ImportExport.getChannelsFromFile(fileLocation);
        for (Channel channel : importedChannels) {
            if (!channels.containsChannel(channel.getId())) {
                channels.add(channel);
                visibleChannels.add(channel);
            }
            for (String tag : channel.getTags()) {
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            }
        }
        Collections.sort(tags);
        channels.write();
        return importedChannels.size();
    }
}
