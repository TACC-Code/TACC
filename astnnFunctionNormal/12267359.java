class BackupThread extends Thread {
    public void setDataGrid(IDataGrid grid, int var, Color c) {
        FigurePanel xp = this.getFigurePanel();
        if (xp != null) {
            vf = (VisualizerFrame) xp.getGraphicalViewer();
            if (vf != null) {
                vf.showWaitPanel("Preparing Kernel Density. Please Wait ...");
            }
        }
        this.sourceGrid = grid;
        usedGridVariables.add(var);
        double[] data = GridUtils.get1DColumnArray(grid, var);
        KernelDensity1D kd = new KernelDensity1D(data, this.bandWidth);
        int n = 50;
        double[] wcopy = data.clone();
        Arrays.sort(wcopy);
        double xmin = wcopy[0];
        double xmax = wcopy[wcopy.length - 1];
        double[][] XY = new double[n][2];
        XY[0][0] = xmin;
        XY[0][1] = kd.f(xmin);
        double[] xa = new double[n];
        double[] ya = new double[n];
        for (int i = 1; i < n; i++) {
            double x = xmin + (double) i / (double) n * (xmax - xmin);
            double y = kd.f(x);
            XY[i][0] = x;
            XY[i][1] = y;
            xa[i] = x;
            ya[i] = y;
        }
        log.debug("area : " + NumericalUtils.trapezoidRule(xa, ya));
        plot2DCanvas.addScatterPlot("debug", Color.orange, XY);
        plot2DCanvas.addLinePlot("Kernel Density", Color.blue, XY);
        if (vf != null) {
            vf.hideWaitPanel();
        }
    }
}
