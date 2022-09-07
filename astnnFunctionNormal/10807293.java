class BackupThread extends Thread {
    public void createControl(Composite parent) {
        canvas = new Canvas(parent, SWT.BORDER);
        LightweightSystem lws = new LightweightSystem(canvas);
        RootEditPart root = editor.getGraphicalViewer().getRootEditPart();
        ScalableFreeformRootEditPart scalable = (ScalableFreeformRootEditPart) root;
        thumbnail = new ScrollableThumbnail((Viewport) scalable.getFigure());
        thumbnail.setSource(scalable.getLayer(LayerConstants.PRINTABLE_LAYERS));
        lws.setContents(thumbnail);
        disposeListener = new DisposeListener() {

            @Override
            public void widgetDisposed(DisposeEvent e) {
                if (thumbnail != null) {
                    thumbnail.deactivate();
                    thumbnail = null;
                }
            }
        };
        Control control = editor.getGraphicalViewer().getControl();
        control.addDisposeListener(disposeListener);
    }
}
