class BackupThread extends Thread {
    protected ResourceManager getResourceManager() {
        return ((DiagramGraphicalViewer) getEditPart().getViewer()).getResourceManager();
    }
}
