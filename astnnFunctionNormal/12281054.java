class BackupThread extends Thread {
    private static int findElementsInDiagramByID(DiagramEditPart diagramPart, EObject element, List editPartCollector) {
        IDiagramGraphicalViewer viewer = (IDiagramGraphicalViewer) diagramPart.getViewer();
        final int intialNumOfEditParts = editPartCollector.size();
        if (element instanceof View) {
            EditPart editPart = (EditPart) viewer.getEditPartRegistry().get(element);
            if (editPart != null) {
                editPartCollector.add(editPart);
                return 1;
            }
        }
        String elementID = EMFCoreUtil.getProxyID(element);
        List associatedParts = viewer.findEditPartsForElement(elementID, IGraphicalEditPart.class);
        for (Iterator editPartIt = associatedParts.iterator(); editPartIt.hasNext(); ) {
            EditPart nextPart = (EditPart) editPartIt.next();
            EditPart parentPart = nextPart.getParent();
            while (parentPart != null && !associatedParts.contains(parentPart)) {
                parentPart = parentPart.getParent();
            }
            if (parentPart == null) {
                editPartCollector.add(nextPart);
            }
        }
        if (intialNumOfEditParts == editPartCollector.size()) {
            if (!associatedParts.isEmpty()) {
                editPartCollector.add(associatedParts.iterator().next());
            } else {
                if (element.eContainer() != null) {
                    return findElementsInDiagramByID(diagramPart, element.eContainer(), editPartCollector);
                }
            }
        }
        return editPartCollector.size() - intialNumOfEditParts;
    }
}
