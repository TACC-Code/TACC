class BackupThread extends Thread {
    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getGraphicalViewer().setContents(score.getPieces().get(0));
    }
}
