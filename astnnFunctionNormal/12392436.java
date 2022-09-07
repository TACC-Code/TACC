class BackupThread extends Thread {
    protected ScalableRootEditPart getRootEditPart() {
        return (ScalableRootEditPart) getGraphicalViewer().getRootEditPart();
    }
}
