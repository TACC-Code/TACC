class BackupThread extends Thread {
    protected void configureGraphicalViewer() {
        getGraphicalViewer().getControl().setBackground(ColorConstants.listBackground);
        GraphicalViewer viewer = getGraphicalViewer();
    }
}
