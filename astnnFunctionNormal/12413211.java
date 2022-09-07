class BackupThread extends Thread {
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == ZoomManager.class && getGraphicalViewer() != null) {
            return getGraphicalViewer().getProperty(ZoomManager.class.toString());
        }
        if (adapter == IContentOutlinePage.class && getGraphicalViewer() != null) {
            return new OverviewOutlinePage(this);
        }
        if (adapter == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
        }
        if (adapter == IArchimateModel.class && getModel() != null) {
            return getModel().getArchimateModel();
        }
        if (adapter == IDiagramModel.class) {
            return getModel();
        }
        return super.getAdapter(adapter);
    }
}
