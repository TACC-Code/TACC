class BackupThread extends Thread {
    @SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
    static void setupHome() {
        File homeDir = new File(SharedGlobal.APP_HOME_DIR);
        if (!homeDir.exists()) {
            homeDir.mkdirs();
        }
        boolean b1 = homeDir.setReadable(false, false);
        boolean b2 = homeDir.setWritable(false, false);
        boolean b3 = homeDir.setExecutable(false, false);
        boolean b4 = homeDir.setReadable(true, true);
        boolean b5 = homeDir.setWritable(true, true);
        boolean b6 = homeDir.setExecutable(true, true);
        if (!b1 || !b2 || !b3 || !b4 || !b5 || !b6) {
            System.err.println("Warning: Can't secure the directory " + homeDir + ". Results= " + b1 + ' ' + b2 + ' ' + b3 + ' ' + b4 + ' ' + b5 + ' ' + b6);
        }
        if (!homeDir.canRead() || !homeDir.canWrite() || !homeDir.canExecute()) {
            System.err.println("Warning: Can't access the directory " + homeDir + " fully. This may result in other error messages. read=" + homeDir.canRead() + " write=" + homeDir.canWrite() + " exec=" + homeDir.canExecute());
        }
        File logDir = new File(Global.LOG_DIR);
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
    }
}
