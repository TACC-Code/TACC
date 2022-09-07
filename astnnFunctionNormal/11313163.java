class BackupThread extends Thread {
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        final GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new BigraphEditPartFactory());
        viewer.setRootEditPart(new ScalableRootEditPart());
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
    }
}
