class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == GraphicalViewer.class) {
            return getGraphicalViewer();
        } else if (adapter == CommandStack.class) {
            return getCommandStack();
        } else if (adapter == ZoomManager.class) {
            return getZoomManager();
        } else {
            return super.getAdapter(adapter);
        }
    }
}
