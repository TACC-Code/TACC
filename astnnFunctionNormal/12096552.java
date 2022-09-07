class BackupThread extends Thread {
    public void run() {
        GraphicalViewer viewer;
        viewer = (GraphicalViewer) getEditorPart().getAdapter(GraphicalViewer.class);
        PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
        PrinterData data = dialog.open();
        if (data != null) {
            DiagramPrintOperation op = new DiagramPrintOperation(new Printer(data), viewer);
            op.run(getEditorPart().getTitle());
        }
    }
}
