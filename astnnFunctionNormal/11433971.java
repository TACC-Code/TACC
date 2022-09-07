class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class type) {
        if (type == IContentOutlinePage.class) return new OutlinePage();
        if (type == ZoomManager.class) return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        return super.getAdapter(type);
    }
}
