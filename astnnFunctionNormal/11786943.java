class BackupThread extends Thread {
    public ZoomInAction(IWorkbenchPart part, GraphicalViewer graphicalViewer) {
        super(part, graphicalViewer, UIType.zoom_in, ID);
    }
}
