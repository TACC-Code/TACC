class BackupThread extends Thread {
    public void setDemo(String dtdfilename) {
        demo = new ICDEDemo(dtdfilename, this);
        textarea.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        pm = new PerformanceMonitor(demo.getFilteringtime(), demo.getThroughput());
        WindowListener pml = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                pm.hide();
            }
        };
        pm.addWindowListener(pml);
        sm = new SystemMonitor();
        WindowListener sml = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                sm.hide();
            }
        };
        sm.addWindowListener(sml);
        wm = new WorkloadMonitor(demo.getEXfilter());
        WindowListener wml = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                wm.hide();
            }
        };
        wm.addWindowListener(wml);
        queryviewer = new QueryViewer(demo);
        WindowListener qvl = new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                queryviewer.hide();
            }
        };
        queryviewer.addWindowListener(qvl);
        queryviewer.waitForDisplay();
        Thread initThread = new Thread() {

            public void run() {
                bulkLoad();
                demo.start();
                writeln("Demo is ready to be run.");
                menubar.enableRun();
                menubar.setModeEnabled(true);
                menubar.setParamEnabled(true);
                menubar.setMonitorEnabled(true);
                run.setEnabled(true);
                cycle.setEnabled(true);
                step.setEnabled(true);
                query.setEnabled(true);
                allqueries.setEnabled(true);
                doc.setEnabled(true);
                textarea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        initThread.start();
    }
}
