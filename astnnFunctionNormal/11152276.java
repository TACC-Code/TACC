class BackupThread extends Thread {
    public Preview(Item item) {
        showingItem = true;
        this.title = item.getTitle();
        this.link = item.getLink();
        this.date = item.getDate();
        this.description = item.getDescription();
        this.author = item.getAuthor();
        this.source = null;
        this.channelTitle = JReader.getChannel(item.getChannelId()).getTitle();
        this.baseURL = JReader.getChannel(item.getChannelId()).getLink();
    }
}
