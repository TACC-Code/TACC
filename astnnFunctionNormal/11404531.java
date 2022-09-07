class BackupThread extends Thread {
    public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
        if (aSelection == null || aSelection.isEmpty()) {
            return;
        }
        if (false == aSelection.getFirstElement() instanceof CallGraphAbstractNavigatorItem) {
            return;
        }
        CallGraphAbstractNavigatorItem abstractNavigatorItem = (CallGraphAbstractNavigatorItem) aSelection.getFirstElement();
        View navigatorView = null;
        if (abstractNavigatorItem instanceof CallGraphNavigatorItem) {
            navigatorView = ((CallGraphNavigatorItem) abstractNavigatorItem).getView();
        } else if (abstractNavigatorItem instanceof CallGraphNavigatorGroup) {
            CallGraphNavigatorGroup navigatorGroup = (CallGraphNavigatorGroup) abstractNavigatorItem;
            if (navigatorGroup.getParent() instanceof CallGraphNavigatorItem) {
                navigatorView = ((CallGraphNavigatorItem) navigatorGroup.getParent()).getView();
            }
        }
        if (navigatorView == null) {
            return;
        }
        IEditorInput editorInput = getEditorInput(navigatorView.getDiagram());
        IEditorPart editor = aPage.findEditor(editorInput);
        if (editor == null) {
            return;
        }
        aPage.bringToTop(editor);
        if (editor instanceof DiagramEditor) {
            DiagramEditor diagramEditor = (DiagramEditor) editor;
            ResourceSet diagramEditorResourceSet = diagramEditor.getEditingDomain().getResourceSet();
            EObject selectedView = diagramEditorResourceSet.getEObject(EcoreUtil.getURI(navigatorView), true);
            if (selectedView == null) {
                return;
            }
            GraphicalViewer graphicalViewer = (GraphicalViewer) diagramEditor.getAdapter(GraphicalViewer.class);
            EditPart selectedEditPart = (EditPart) graphicalViewer.getEditPartRegistry().get(selectedView);
            if (selectedEditPart != null) {
                graphicalViewer.select(selectedEditPart);
            }
        }
    }
}
