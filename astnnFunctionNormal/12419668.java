class BackupThread extends Thread {
    public Object getAdapter(Class type) {
        if (type == CommandStackInspectorPage.class) return new CommandStackInspectorPage(getCommandStack());
        if (type == IContentOutlinePage.class) return new OutlinePage(new TreeViewer());
        if (type == ZoomManager.class) return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
        return super.getAdapter(type);
    }
}
