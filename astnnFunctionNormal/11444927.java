class BackupThread extends Thread {
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        getDiagramGraphicalViewer().addDropTargetListener(new DropTargetListener(getDiagramGraphicalViewer(), LocalSelectionTransfer.getTransfer()) {

            protected Object getJavaObject(TransferData data) {
                return LocalSelectionTransfer.getTransfer().nativeToJava(data);
            }
        });
        getDiagramGraphicalViewer().addDropTargetListener(new DropTargetListener(getDiagramGraphicalViewer(), LocalTransfer.getInstance()) {

            protected Object getJavaObject(TransferData data) {
                return LocalTransfer.getInstance().nativeToJava(data);
            }
        });
    }
}
