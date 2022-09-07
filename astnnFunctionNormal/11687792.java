class BackupThread extends Thread {
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        default_.diagram.part.DiagramEditorContextMenuProvider provider = new default_.diagram.part.DiagramEditorContextMenuProvider(this, getDiagramGraphicalViewer());
        getDiagramGraphicalViewer().setContextMenu(provider);
        getSite().registerContextMenu(ActionIds.DIAGRAM_EDITOR_CONTEXT_MENU, provider, getDiagramGraphicalViewer());
    }
}
