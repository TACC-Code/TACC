class BackupThread extends Thread {
    protected void initializeGraphicalViewer(Composite parent) {
        ScrollingGraphicalViewer viewer = getGraphicalViewer();
        viewer.createControl(parent);
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.getControl().setBackground(ColorConstants.white);
        getEditDomain().addViewer(viewer);
        getSite().setSelectionProvider(viewer);
        viewer.setEditPartFactory(getEditPartFactory());
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
    }
}
