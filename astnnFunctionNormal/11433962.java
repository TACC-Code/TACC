class BackupThread extends Thread {
        @SuppressWarnings("synthetic-access")
        @Override
        public void createControl(Composite parent) {
            sashForm = new SashForm(parent, SWT.VERTICAL);
            EditPartViewer viewer = getViewer();
            viewer.createControl(sashForm);
            viewer.setEditDomain(getEditDomain());
            viewer.setEditPartFactory(new TreeFactory());
            viewer.setContents(model);
            getSelectionSynchronizer().addViewer(viewer);
            Canvas canvas = new Canvas(sashForm, SWT.BORDER);
            LightweightSystem lws = new LightweightSystem(canvas);
            ScalableRootEditPart rootEditPart = (ScalableRootEditPart) getGraphicalViewer().getRootEditPart();
            thumbnail = new ScrollableThumbnail((Viewport) rootEditPart.getFigure());
            thumbnail.setSource(rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS));
            lws.setContents(thumbnail);
            disposeListener = new DisposeListener() {

                public void widgetDisposed(DisposeEvent e) {
                    if (thumbnail != null) {
                        thumbnail.deactivate();
                        thumbnail = null;
                    }
                }
            };
            getGraphicalViewer().getControl().addDisposeListener(disposeListener);
        }
}
