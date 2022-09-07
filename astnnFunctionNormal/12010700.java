class BackupThread extends Thread {
    @Override
    public void createPartControl(Composite parent) {
        createGraphicalViewer(parent);
    }
}
