class BackupThread extends Thread {
    @Override
    protected boolean calculateEnabled() {
        GraphicalViewer viewer = this.getGraphicalViewer();
        for (Object object : viewer.getSelectedEditParts()) {
            if (object instanceof ConnectionEditPart) {
                return true;
            } else if (object instanceof NodeElementEditPart) {
                NodeElementEditPart nodeElementEditPart = (NodeElementEditPart) object;
                if (!nodeElementEditPart.getSourceConnections().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}
