class BackupThread extends Thread {
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setContents(getModel());
        ScalableFreeformRootEditPart root = (ScalableFreeformRootEditPart) viewer.getRootEditPart();
        ConnectionLayer connLayer = (ConnectionLayer) root.getLayer(LayerConstants.CONNECTION_LAYER);
        GraphicalEditPart contentEditPart = (GraphicalEditPart) root.getContents();
        ShortestPathConnectionRouter router = new ShortestPathConnectionRouter(contentEditPart.getFigure());
        connLayer.setConnectionRouter(router);
        viewer.addDropTargetListener(createTransferDropTargetListener());
    }
}
