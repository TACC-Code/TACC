class BackupThread extends Thread {
    public void start() throws Exception {
        ArrayList list = new ArrayList();
        for (int i = 0; i < directoryList.length; i++) {
            File dir = directoryList[i];
            File[] files = dir.listFiles();
            for (int j = 0; j < files.length; j++) {
                list.add(files[j]);
            }
        }
        existingRecoveryLogFiles = (File[]) list.toArray(new File[list.size()]);
        logCleaner = new LogRestarter();
        new Thread(logCleaner, "Log file cleaner").start();
        writers = new BatchWriter[directoryList.length];
        String branchQualifier = xidFactory.getBranchQualifier();
        for (int i = 0; i < directoryList.length; i++) {
            writers[i] = new BatchWriter(branchQualifier, logFileSize / 128, directoryList[i], logFileSize, logCleaner);
            new Thread(writers[i], "Batch Recovery Log " + i).start();
        }
        numWriters = writers.length;
        existingHeuristicStatusLogFiles = heuristicStatusLogDirectory.listFiles();
        heuristicStatusLogger = new HeuristicStatusLog(heuristicStatusLogDirectory);
    }
}
