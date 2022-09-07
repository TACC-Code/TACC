class BackupThread extends Thread {
    private boolean isGraphicalViewer() {
        return getCurrentViewer() instanceof GraphicalViewer;
    }
}
