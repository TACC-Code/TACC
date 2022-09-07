class BackupThread extends Thread {
    public LayoutOperation(GraphicalViewer viewer) {
        super(Messages.Operation_LayoutTables);
        this.viewer = viewer;
    }
}
