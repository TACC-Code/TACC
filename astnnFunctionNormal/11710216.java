class BackupThread extends Thread {
    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        GraphicalViewer viewer = getGraphicalViewer();
        getGraphicalViewer().setEditPartFactory(new ScoreEditPartFactory());
        double[] zoomLevels = new double[] { 0.375, 0.5, 0.625, 0.75, 1, 1.25, 1.5, 2, 2.5, 3, 4, 5, 6 };
        getZoomManager().setZoomLevels(zoomLevels);
        scaleZoomLevels(Engraver.DISPLAYED_FONT_SIZE / Engraver.FONT_SIZE);
        ContextMenuProvider contextMenuProvider = new ScoreEditorContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(contextMenuProvider);
        getSite().registerContextMenu(contextMenuProvider, viewer);
        KeyHandler keyHandler = new KeyHandler();
        keyHandler.put(KeyStroke.getPressed(SWT.DEL, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        keyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
        getGraphicalViewer().setKeyHandler(keyHandler);
    }
}
