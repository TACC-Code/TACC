class BackupThread extends Thread {
    public Preview(Channel ch) {
        showingItem = false;
        this.title = ch.getTitle();
        this.link = ch.getLink();
        this.description = ch.getDescription();
        this.imageURL = ch.getImageURL();
        this.imageTitle = ch.getImageTitle();
        this.imageLink = ch.getImageLink();
        this.source = ch.getChannelURL();
        this.baseURL = ch.getLink();
    }
}
