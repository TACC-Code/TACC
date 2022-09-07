class BackupThread extends Thread {
    public ChangeTrackingDialog(Shell parentShell, GraphicalViewer viewer, ERDiagram diagram) {
        super(parentShell);
        this.viewer = viewer;
        this.diagram = diagram;
    }
}
