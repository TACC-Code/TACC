class BackupThread extends Thread {
    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        final GraphicalViewer graphicalViewer = getGraphicalViewer();
        graphicalViewer.setContents(getBigraph());
    }
}
