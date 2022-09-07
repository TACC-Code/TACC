class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class adapter) {
        if (GraphicalViewer.class.equals(adapter)) {
            if (myVisualizer != null) {
                return myVisualizer.getViewer();
            } else {
                return null;
            }
        }
        return super.getAdapter(adapter);
    }
}
