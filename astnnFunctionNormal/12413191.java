class BackupThread extends Thread {
    protected void setProperties() {
        applyUserGridPreferences();
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
    }
}
