class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    protected EditPart getPartFor(ProcessStep step) {
        List steps = getGraphicalViewer().getContents().getChildren();
        for (Object s : steps) {
            EditPart editPart = (EditPart) s;
            if (editPart.getModel() == step) return editPart;
        }
        return null;
    }
}
