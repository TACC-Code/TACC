class BackupThread extends Thread {
    public Server() throws RemoteException {
        fileLog = new Logger(logPath);
        auth = new Authenticator();
        demonstratorKeys = new LinkedList<String>();
        studentKeys = new LinkedList<String>();
        runningLabs = new LinkedList<Lab>();
        try {
            ConfigFile config = new ConfigFile(cPath);
            authMethod = config.getValue(cAuthKey);
        } catch (Exception e) {
            write("Error reading server configuration file.");
        }
    }
}
