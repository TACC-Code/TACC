class BackupThread extends Thread {
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        viewer.setEditPartFactory(new Factory());
        ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
        viewer.setRootEditPart(rootEditPart);
        ZoomManager zoomManager = rootEditPart.getZoomManager();
        getActionRegistry().registerAction(new ZoomInAction(zoomManager));
        getActionRegistry().registerAction(new ZoomOutAction(zoomManager));
        zoomManager.setZoomLevels(new double[] { 0.25, 0.5, 0.75, 1.0, 1.25, 1.5 });
        ArrayList<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        zoomManager.setZoomLevelContributions(zoomContributions);
    }
}
