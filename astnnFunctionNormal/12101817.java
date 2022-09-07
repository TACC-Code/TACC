class BackupThread extends Thread {
    public static org.eclipse.jface.util.TransferDropTargetListener forImport(GraphicalViewer viewer) {
        return new DiagramDropTargetListener(viewer, ImportTransfer.getInstance());
    }
}
