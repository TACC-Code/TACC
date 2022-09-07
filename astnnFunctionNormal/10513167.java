class BackupThread extends Thread {
    protected Control getGraphicalControl() {
        return getGraphicalViewer().getControl();
    }
}
