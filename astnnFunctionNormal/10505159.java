class BackupThread extends Thread {
    protected boolean isViewerImportant(EditPartViewer viewer) {
        return viewer instanceof GraphicalViewer;
    }
}
