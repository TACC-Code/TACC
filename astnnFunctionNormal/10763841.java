class BackupThread extends Thread {
    public void runModules() {
        if (modules == null) {
            readModuleNames();
        }
        if (!LibLoader.isRunning()) {
            try {
                LibLoader.startProcess();
            } catch (IOException ioe) {
                Log.war("Cannot run libLoader", Log.WARNING);
                ioe.printStackTrace(System.err);
                return;
            }
        }
        for (String module : modules) {
            try {
                LibLoader.loadLibrary(module);
                LibLoader.setFunction("run", null);
                LibLoader.runThread(SSettings.getInt(FileUtils.writeMaxProcDelay, FileUtils.defaultMaxProcDelay));
                if (LibLoader.isRunning()) {
                    LibLoader.unloadLibrary();
                }
            } catch (IOException ioe) {
                Log.out("runModules: " + ioe.getClass().getName() + " " + ioe.getMessage(), Log.WARNING);
                try {
                    LibLoader.stopProcess();
                } catch (IOException ex) {
                    Log.out("stopProcess: " + ex.getMessage(), Log.WARNING);
                }
                try {
                    LibLoader.startProcess();
                } catch (IOException ex) {
                    Log.out("startProcess: " + ex.getMessage(), Log.WARNING);
                }
            }
        }
    }
}
