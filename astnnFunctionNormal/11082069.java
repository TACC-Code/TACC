class BackupThread extends Thread {
    protected ERDiagram getDiagram() {
        GraphicalViewer graphicalViewer = getDesignerEditor().getGraphicalViewer();
        ERDiagramEditPart diagramEditPart = (ERDiagramEditPart) graphicalViewer.getContents();
        return (ERDiagram) diagramEditPart.getModel();
    }
}
