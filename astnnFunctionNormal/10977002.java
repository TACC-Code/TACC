class BackupThread extends Thread {
    private static void updateTagsList() {
        tags.clear();
        for (Channel channel : channels.getChannels()) {
            for (String tag : channel.getTags()) {
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            }
        }
        Collections.sort(tags);
    }
}
