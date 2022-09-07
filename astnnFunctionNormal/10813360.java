class BackupThread extends Thread {
    public CopyAsImageAction(GraphicalViewer viewer) {
        super(DBPlugin.getResourceString("action.copyAsImage"));
        this.viewer = viewer;
    }
}
