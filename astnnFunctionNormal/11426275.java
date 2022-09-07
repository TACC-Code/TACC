class BackupThread extends Thread {
    public ZoomFitAction(IWorkbenchPart part, GraphicalViewer graphicalViewer) {
        super(part, graphicalViewer, UIType.zoom_fit, ID);
    }
}
