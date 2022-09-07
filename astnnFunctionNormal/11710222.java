class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    @Override
    public Object getAdapter(Class type) {
        if (type == PartsPage.class) {
            return new PartsPage(getEditDomain(), score.getPieces().get(0), getActionRegistry());
        } else if (type == MovementsPage.class) {
            return new MovementsPage(getEditDomain(), score, getGraphicalViewer());
        } else if (type == OverviewPage.class) {
            return getOverviewPage();
        } else {
            return super.getAdapter(type);
        }
    }
}
