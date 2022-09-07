class BackupThread extends Thread {
    @Override
    public void setFocus() {
        getGraphicalViewer().getControl().setFocus();
    }
}
