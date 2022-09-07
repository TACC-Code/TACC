class BackupThread extends Thread {
    public void saveProblem(ProblemWriter output) {
        String filenameToSave = "swing.iwp";
        try {
            IWPLog.info(this, "[TEST_SwingXmlRpcClient] I'm being told to save my current problem");
            output.writeProblem(readProblemFile(filenameToSave));
        } catch (DataStoreException e) {
            IWPLog.info(this, "[TEST_SwingXmlRpcClient] Unable to load problem: " + filenameToSave);
        }
    }
}
