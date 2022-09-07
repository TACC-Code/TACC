class BackupThread extends Thread {
    public static org.eclipse.jface.util.TransferDropTargetListener forModel(GraphicalViewer viewer) {
        return new DiagramDropTargetListener(viewer, ModelTransfer.getInstance());
    }
}
