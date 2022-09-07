class BackupThread extends Thread {
    public void dispose() {
        editor.getSelectionSynchronizer().removeViewer(getViewer());
        Control control = editor.getGraphicalViewer().getControl();
        if (control != null && !control.isDisposed()) {
            control.removeDisposeListener(disposeListener);
        }
        super.dispose();
    }
}
