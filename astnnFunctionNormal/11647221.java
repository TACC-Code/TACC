class BackupThread extends Thread {
    @Override
    public void run() {
        String serverVersion = PawServer.class.getPackage().getImplementationVersion();
        String msg = "220 PAW " + serverVersion + " Admin Server on port " + port;
        try {
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            out.write((msg + "\r\n").getBytes());
            String line;
            String userString = "", passString = "";
            line = readLine(in);
            if (line.startsWith("user") && line.indexOf(" ") != -1) userString = line.substring(5);
            line = readLine(in);
            if (line.startsWith("pass") && line.indexOf(" ") != -1) passString = line.substring(5);
            if (!userString.equals(this.user) || !passString.equals(this.pass)) {
                out.write(("403 Login failed" + "\r\n").getBytes());
                socket.close();
                return;
            }
            out.write(("240 Login correct" + "\r\n").getBytes());
            while ((line = readLine(in)) != null) {
                if (line.equals("quit")) {
                    out.write(("221 PAW Admin Server closing" + " connection\r\n").getBytes());
                    break;
                } else if (line.startsWith("status")) {
                    outputServerStatus(out);
                    out.write(("226 End of Server Status" + "\r\n").getBytes());
                } else if (line.equals("server stop")) {
                    if (pawServer.serverStarted) {
                        pawServer.server.listen.close();
                        pawServer.serverStarted = false;
                        out.write(("200 Server has been stopped" + "\r\n").getBytes());
                    } else out.write(("400 Server already stopped" + "\r\n").getBytes());
                } else if (line.equals("server start")) {
                    if (!pawServer.serverStarted) {
                        pawServer.startServer();
                        pawServer.serverStarted = true;
                        out.write(("200 Server has been started" + "\r\n").getBytes());
                    } else out.write(("400 Server already running" + "\r\n").getBytes());
                } else if (line.equals("admin restart")) {
                    out.write(("200 Admin server will be restarted" + "\r\n").getBytes());
                    pawAdmin.user = pawServer.adminUser;
                    pawAdmin.pass = pawServer.adminPass;
                    pawAdmin.port = Integer.decode(pawServer.adminPort).intValue();
                    boolean restartNecessary = pawAdmin.lastPortUsed != pawAdmin.port ? true : false;
                    if (restartNecessary || !pawServer.adminActive) {
                        pawAdmin.closeAllSockets();
                        pawAdmin.adminSocket.close();
                    }
                    if (pawServer.adminActive && restartNecessary) pawAdmin.startAdmin();
                    break;
                } else if (line.equals("init")) {
                    pawServer.init();
                    out.write(("200 Init sent to server" + "\r\n").getBytes());
                } else if (line.equals("shutdown")) {
                    out.write(("200 Shutting down server" + "\r\n").getBytes());
                    System.exit(0);
                } else if (line.startsWith("getconf") && line.indexOf(" ") != -1) {
                    String filename = line.substring(8);
                    if (!readFile("conf", filename, out)) out.write(("400 Error while accessing File " + filename + "\r\n").getBytes()); else out.write(("226 Output of file " + filename + " completed" + "\r\n").getBytes());
                } else if (line.startsWith("getlog") && line.indexOf(" ") != -1) {
                    String filename = line.substring(7);
                    if (!readFile("logs", filename, out)) out.write(("400 Error while accessing LogFile " + filename + "\r\n").getBytes()); else out.write(("226 Output of logfile " + filename + " completed" + "\r\n").getBytes());
                } else if (line.startsWith("put") && line.indexOf(" ") != -1) {
                    String filename = line.substring(4);
                    out.write(("200 Singel . in a new line means EOF" + "\r\n").getBytes());
                    if (!putFile(filename, in)) out.write(("400 Error while writing File " + filename + "\r\n").getBytes()); else out.write(("226 File " + filename + " has been written" + "\r\n").getBytes());
                } else if (line.startsWith("clearlog")) {
                    pawServer.redirectStdout();
                    out.write(("226 Logfile has been cleared\r\n").getBytes());
                } else if (line.equals("getBrazilConfig")) {
                    Enumeration e = pawServer.config.keys();
                    while (e.hasMoreElements()) {
                        String key = (String) e.nextElement();
                        String value = (String) pawServer.config.get(key);
                        out.write((key + "=" + value + "\n").getBytes());
                    }
                    out.write(("226 End of Brazil config file\r\n").getBytes());
                } else out.write(("500 Command unrecognized: " + "\"" + line + "\"" + "\r\n").getBytes());
            }
            socket.close();
        } catch (IOException e) {
        } catch (NullPointerException ne) {
        }
    }
}
