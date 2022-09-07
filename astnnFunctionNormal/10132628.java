class BackupThread extends Thread {
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            File outputLogDir = new File("/OutputLogs");
            outputLogDir.createNewFile();
            FileUtils.copyFile(logFile.getAbsolutePath(), outputLogDir + "/" + logFileName);
        } else if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
            File outputLogDir = new File("c:\\OutputLogs");
            outputLogDir.createNewFile();
            FileUtils.copyFile(logFile.getAbsolutePath(), outputLogDir + "/" + logFileName);
        }
    }
}
