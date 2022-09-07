class BackupThread extends Thread {
    public void setViewer(EditPartViewer viewer) {
        if (viewer == getCurrentViewer()) return;
        super.setViewer(viewer);
        if (viewer instanceof GraphicalViewer) setDefaultCursor(SharedCursors.CROSS); else setDefaultCursor(SharedCursors.NO);
    }
}
