class BackupThread extends Thread {
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        DiagramRootEditPart root = (DiagramRootEditPart) getDiagramGraphicalViewer().getRootEditPart();
        OwlsEditPartFactory.setupConnectionLayerExEx(root);
    }
}
