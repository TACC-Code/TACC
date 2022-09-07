class BackupThread extends Thread {
    private void execute() throws SSHException, java.io.IOException {
        System.err.println("TODO: SSH.java:execute() SSL init");
        Log.init(options.getLogLevel(), true);
        if (cmdOptions.getConfig() != null) {
            options.readConfigFile(cmdOptions.getConfig(), cmdOptions.getHost());
        } else {
            options.readConfigFile(System.getProperty("home.dir") + "/" + Options.PATH_SSH_USER_CONFFILE, cmdOptions.getHost());
            options.readConfigFile(Options.PATH_HOST_CONFIG_FILE, cmdOptions.getHost());
        }
        options.fillDefaultOptions();
        Log.init(options.getLogLevel(), true);
        Log.getLogInstance().TODO("SSH.execute() seed RNG");
        if (options.getUser() == null) {
            options.setUser(System.getProperty("user.name"));
        }
        if (options.getHostname() != null) {
            cmdOptions.setHost(options.getHostname());
        }
        Log.getLogInstance().TODO("SSH.execute() options manipulation");
        SSHConnection connection = null;
        try {
            Log.getLogInstance().log("TODO: SSH.java:execute() init connection");
            connection = new SSHConnection(options);
            connection.connect(cmdOptions.getHost(), options.getPort(), options.getConnectionAttempts(), options.getProxyCommand());
        } catch (java.net.UnknownHostException e) {
            System.err.println("Couldn't find the host " + cmdOptions.getHost());
            return;
        } catch (java.net.SocketException e) {
            connection.close();
            System.err.println("Error on socket: " + e.getMessage());
            return;
        } catch (ConnectionAbortedException e) {
            System.err.println("Connection aborted");
            return;
        } catch (ConnectionRefusedException e) {
            System.err.println("Connection refused");
            return;
        }
        Log.getLogInstance().log("TODO: SSH.java:execute() load host key");
        Log.getLogInstance().log("TODO: SSH.java:execute() load identity");
        try {
            connection.login(cmdOptions.getHost(), cmdOptions.getUsername());
        } catch (java.io.IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        Log.getLogInstance().log("TODO: SSH.java:execute() clear host keys");
        int exitStatus = 0;
        Log.getLogInstance().log("TODO: SSH.java:execute() initiate session");
        exitStatus = sshSession(connection);
        connection.close();
        System.exit(exitStatus);
    }
}
