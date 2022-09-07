class BackupThread extends Thread {
    public AbstractZoomAction(IWorkbenchPart part, GraphicalViewer viewer, UIType uiType, String id) {
        super(part);
        myViewer = viewer;
        setImageDescriptor(uiType.getImageDescriptor());
        setText(uiType.label);
        setToolTipText(getText());
        setActionDefinitionId(id);
        getZoomer().addZoomListener(this);
    }
}
