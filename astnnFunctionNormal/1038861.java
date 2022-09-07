class BackupThread extends Thread {
    public void propertyChange(PropertyChangeEvent event) {
        String property = event.getProperty();
        if (property.equals(ReportDesignerConstants.GRID_SPACING_PREF)) {
            Integer value = (Integer) event.getNewValue();
            editor.getGraphicalViewer().setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(value.intValue(), value.intValue()));
        } else if (property.equals(ReportDesignerConstants.RULERS_VISIBLE_PREF)) {
            Boolean value = (Boolean) event.getNewValue();
            editor.getGraphicalViewer().setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, value);
        }
    }
}
