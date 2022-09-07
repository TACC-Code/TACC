class BackupThread extends Thread {
    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        this.getGraphicalViewer().removeSelectionChangedListener(listener);
    }
}
