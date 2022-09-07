class BackupThread extends Thread {
    public ToggleGridVisibilityAction(ReportEditor reportEditor, GraphicalViewer viewer) {
        super(GEFMessages.ToggleGrid_Label, 2, reportEditor, viewer);
        setToolTipText(GEFMessages.ToggleGrid_Tooltip);
        setId("org.eclipse.gef.toggle_grid_visibility");
        setActionDefinitionId("org.eclipse.gef.toggle_grid_visibility");
        setChecked(isChecked());
    }
}
