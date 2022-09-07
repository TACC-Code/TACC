class BackupThread extends Thread {
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();
        ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();
        getActionRegistry().registerAction(new ZoomInAction(root.getZoomManager()));
        getActionRegistry().registerAction(new ZoomOutAction(root.getZoomManager()));
        viewer.setRootEditPart(root);
        viewer.setEditPartFactory(new PIMEditPartFactory());
        ContextMenuProvider provider = new PIMDiagramContextMenuProvider(viewer, getActionRegistry());
        viewer.setContextMenu(provider);
        getSite().registerContextMenu("com.metanology.mde.ui.pimEditor.diagrams.editor.contextmenu", provider, viewer);
        viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer).setParent(getCommonKeyHandler()));
    }
}
