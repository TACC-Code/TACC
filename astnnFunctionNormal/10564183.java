class BackupThread extends Thread {
    @Override
    protected void showDiagram() {
        if (myVisualizer == null) {
            assert getEditorInput() instanceof ProcessDesignerEditorInput;
            myRefreshPerformer = new RefreshPerformer(this);
            MenuManager contextMenu = buildContextMenu();
            PaletteRoot palette = buildPalette();
            myInplaceManager = new ProcessDesignerInplaceManager();
            myVisualizer = new Visualizer(myDiagramPane, myModelProvider, contextMenu, getSite().getPage(), palette, myInplaceManager);
            myInplaceManager.setVisualizer(myVisualizer);
            getSite().registerContextMenu(contextMenu, myVisualizer.getSelectionProvider());
            mySelectionProvider.setDelegate(myVisualizer.getSelectionProvider());
            Iterable<Iterable<IAction>> toolbar = buildToolbar((GraphicalViewer) myVisualizer.getViewer());
            myVisualizer.setupActions(toolbar);
        }
        myPageBook.showPage(myDiagramPane);
    }
}
