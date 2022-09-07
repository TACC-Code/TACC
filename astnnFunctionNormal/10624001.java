class BackupThread extends Thread {
    @Override
    public void run() {
        GraphicalViewer viewer;
        viewer = ((ProcessViewEditor) getWorkbenchPart()).getViewer();
        final PrintDialog dialog = new PrintDialog(viewer.getControl().getShell(), SWT.NULL);
        final PrinterData data = dialog.open();
        if (data != null) {
            final PrintGraphicalViewerOperation op = new PrintGraphicalViewerOperation(new Printer(data), viewer);
            op.setPrintMode(PrintFigureOperation.FIT_PAGE);
            op.run(getWorkbenchPart().getTitle());
        }
    }
}
