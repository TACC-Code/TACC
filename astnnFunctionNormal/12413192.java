class BackupThread extends Thread {
    protected void applyUserGridPreferences() {
        int gridSize = Preferences.getGridSize();
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(gridSize, gridSize));
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, Preferences.isGridVisible());
        getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, Preferences.isGridSnap());
        getGraphicalViewer().setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, Preferences.doShowGuideLines());
    }
}
