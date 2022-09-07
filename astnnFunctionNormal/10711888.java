class BackupThread extends Thread {
    public ExportAsImageAction(GraphicalViewer diagramViewer) {
        super(TEXT);
        fDiagramViewer = diagramViewer;
        setId(ID);
    }
}
