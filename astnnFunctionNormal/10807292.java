class BackupThread extends Thread {
    public ThumbnailOutlinePage(GraphEditor editor) {
        super(new GraphicalViewerImpl());
        this.editor = editor;
    }
}
