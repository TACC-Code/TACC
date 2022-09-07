class BackupThread extends Thread {
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer));
        registerContextMenu(viewer);
        createRootEditPart(viewer);
        createActions(viewer);
        viewer.addDropTargetListener(new PaletteTemplateTransferDropTargetListener(this));
        setProperties();
    }
}
