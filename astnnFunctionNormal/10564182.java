class BackupThread extends Thread {
    protected Iterable<Iterable<IAction>> buildToolbar(GraphicalViewer graphicalViewer) {
        ArrayList<Iterable<IAction>> toolbar = new ArrayList<Iterable<IAction>>();
        ArrayList<IAction> navigateActions = new ArrayList<IAction>();
        navigateActions.add(new GoHomeAction(myHistoryTracker));
        navigateActions.add(new GoBackAction(myHistoryTracker));
        navigateActions.add(new GoForwardAction(myHistoryTracker));
        navigateActions.add(new RefreshAction(myRefreshPerformer));
        toolbar.add(navigateActions);
        ArrayList<IAction> zoomActions = new ArrayList<IAction>();
        zoomActions.add(new ZoomInAction(this));
        zoomActions.add(new ZoomOutAction(this));
        zoomActions.add(new ZoomFitAction(this));
        zoomActions.add(new ZoomOneAction(this));
        toolbar.add(zoomActions);
        ArrayList<IAction> viewActions = new ArrayList<IAction>();
        viewActions.add(new ShowChildrenAsListAction(this));
        viewActions.add(new LayoutHorizontalAction(this));
        viewActions.add(new FilterAction(this));
        viewActions.add(new OverviewAction(this, graphicalViewer));
        toolbar.add(viewActions);
        ArrayList<IAction> printActions = new ArrayList<IAction>();
        printActions.add(new PrintAction(this));
        toolbar.add(printActions);
        return toolbar;
    }
}
