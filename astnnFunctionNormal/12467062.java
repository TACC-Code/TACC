class BackupThread extends Thread {
    public static void selectElementsInDiagram(IDiagramWorkbenchPart diagramPart, List<EditPart> editParts) {
        diagramPart.getDiagramGraphicalViewer().deselectAll();
        EditPart firstPrimary = null;
        for (EditPart nextPart : editParts) {
            diagramPart.getDiagramGraphicalViewer().appendSelection(nextPart);
            if (firstPrimary == null && nextPart instanceof IPrimaryEditPart) {
                firstPrimary = nextPart;
            }
        }
        if (!editParts.isEmpty()) {
            diagramPart.getDiagramGraphicalViewer().reveal(firstPrimary != null ? firstPrimary : (EditPart) editParts.get(0));
        }
    }
}
