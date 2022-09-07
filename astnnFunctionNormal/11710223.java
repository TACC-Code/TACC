class BackupThread extends Thread {
    protected OverviewPage getOverviewPage() {
        if (overviewPage == null) {
            overviewPage = new OverviewPage(getGraphicalViewer());
        }
        return overviewPage;
    }
}
