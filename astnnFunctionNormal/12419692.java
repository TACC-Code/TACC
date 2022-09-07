class BackupThread extends Thread {
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        this.getGraphicalViewer().addSelectionChangedListener(listener);
    }
}
