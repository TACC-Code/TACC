class BackupThread extends Thread {
    public String validate() {
        List<ValidationError> validationErrors = process.validate();
        if (validationErrors.size() == 0) return null;
        ValidationError error = validationErrors.get(0);
        EditPart childEditPart = getPartFor((ProcessStep) error.getElement());
        getGraphicalViewer().setSelection(new StructuredSelection(childEditPart));
        return Messages.ProcessEditor_ValidationErrorPreamble + error.getErrorMessage();
    }
}
