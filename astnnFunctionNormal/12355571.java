class BackupThread extends Thread {
    protected void performDirectEdit() {
        if (manager == null) {
            ValidationEnabledGraphicalViewer viewer = (ValidationEnabledGraphicalViewer) getViewer();
            ValidationMessageHandler handler = viewer.getValidationHandler();
            Label l = (Label) getFigure();
            ColumnNameTypeCellEditorValidator columnNameTypeCellEditorValidator = new ColumnNameTypeCellEditorValidator(handler);
            manager = new ExtendedDirectEditManager(this, TextCellEditor.class, new LabelCellEditorLocator(l), l, columnNameTypeCellEditorValidator);
        }
        manager.show();
    }
}
