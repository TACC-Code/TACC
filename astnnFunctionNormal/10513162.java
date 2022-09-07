class BackupThread extends Thread {
    protected void initializeGraphicalViewer() {
        splitter.hookDropTargetListener(getGraphicalViewer());
    }
}
