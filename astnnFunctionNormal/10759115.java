class BackupThread extends Thread {
    public void setLocation(Point p) {
        ProlixScrollingGraphicalViewer test = ((ProlixScrollingGraphicalViewer) editPart.getViewer());
        location = test.getEditor().translateLocationScrollbar(p);
    }
}
