class BackupThread extends Thread {
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
}
