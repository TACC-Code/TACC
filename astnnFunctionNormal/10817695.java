class BackupThread extends Thread {
    public void setGraphicalViewer(GraphicsViewer frame) {
        this.gViewer = frame;
        this.gViewer.updateViewer();
    }
}
