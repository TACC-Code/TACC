class BackupThread extends Thread {
    protected void initializeGraphicalViewer() {
        if (getPIMDiagram() != null) {
            getPIMDiagram().refreshChildren();
            getGraphicalViewer().setContents(getPIMDiagram());
        }
        getGraphicalViewer().addDropTargetListener(new PIMDiagramTemplateTransferDropTargetListener(getGraphicalViewer()));
        getGraphicalViewer().addDropTargetListener(new PIMDiagramExplorerTransferDropTargetListener(this, getGraphicalViewer()));
    }
}
