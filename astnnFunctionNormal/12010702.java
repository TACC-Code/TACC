class BackupThread extends Thread {
    protected void setGraphicalViewer(GraphicalViewer viewer) {
        getEditDomain().addViewer(viewer);
        this.graphicalViewer = viewer;
    }
}
