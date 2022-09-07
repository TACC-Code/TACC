class BackupThread extends Thread {
        @SuppressWarnings("synthetic-access")
        @Override
        public void dispose() {
            getSelectionSynchronizer().removeViewer(getViewer());
            Control control = getGraphicalViewer().getControl();
            if (control != null && !control.isDisposed()) control.removeDisposeListener(disposeListener);
            super.dispose();
        }
}
