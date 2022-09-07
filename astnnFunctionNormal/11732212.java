class BackupThread extends Thread {
    private void buildCommandChain() {
        GraphicalViewer gefeditor = role.getLearningDesignDataModel().getEditor().getGraphicalViewer();
        List editparts = gefeditor.getSelectedEditParts();
        Iterator it = editparts.iterator();
        while (it.hasNext()) {
            Object editpart = it.next();
            if (editpart instanceof ActivityEditPart) {
                ModelDiagramActivity activity = ((ActivityEditPart) editpart).getModelPlayActivity();
                ChangeRoleCommand command = new ChangeRoleCommand();
                command.set_role(role);
                command.setElement(activity);
                addCommand(command);
            }
        }
    }
}
