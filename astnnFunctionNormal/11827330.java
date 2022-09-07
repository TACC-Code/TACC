class BackupThread extends Thread {
    public void run() {
        try {
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).showWaitPanel("Preparing PDEScatter. Please Wait ...");
            ExperimentNode eNode = this.getFigurePanel().getSourceNode();
            IDataNode dn = eNode.getMethod();
            IDataGrid dataGrid = (IDataGrid) dn.getOutput(IDataGrid.class);
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).showWaitPanel("Preparing PDEScatter. Please Wait ...");
            IDataGrid subgrid = dataGrid.getSubGrid(new int[] { var1, var2 }, dataGrid.getNumRows());
            this.pdens.setDataGrid(subgrid);
            pdens.setParetoRadius();
            log.debug("build up dens _ max: " + Univariate.getMax(pdens.getDensities()));
            this.setDataSource(dataGrid, var1, var2);
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).hideWaitPanel();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).initToolbox();
        } catch (Exception e) {
            e.printStackTrace();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).hideWaitPanel();
            ((VisualizerFrame) this.getFigurePanel().getGraphicalViewer()).dispose();
            System.err.println("PDEScatter : Something went wrong. Please note the following error messages :");
            System.err.println(e);
            e.printStackTrace();
        }
    }
}
