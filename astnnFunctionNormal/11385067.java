class BackupThread extends Thread {
    @Override
    public void run() {
        GraphicalViewer viewer = getViewer();
        RootEditPart root = viewer.getRootEditPart();
        if (!(root instanceof ScalableFreeformRootEditPart)) {
            return;
        }
        ScalableFreeformRootEditPart scalableRoot = (ScalableFreeformRootEditPart) root;
        ZoomManager zoomer = scalableRoot.getZoomManager();
        run(zoomer);
        Viewport vp = (Viewport) scalableRoot.getFigure();
        setSavePositionFlag(vp.getVerticalRangeModel());
        setSavePositionFlag(vp.getHorizontalRangeModel());
        EditPart contents = viewer.getContents();
        if (contents != null) {
            contents.refresh();
        }
    }
}
