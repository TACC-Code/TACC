class BackupThread extends Thread {
    protected void startConnection(final Host host) {
        this.host = host;
        new Thread(new Runnable() {

            public void run() {
                byte[] b = new byte[4096];
                try {
                    String hostProtocal = host.getProtocal();
                    String hostHost = host.getHost();
                    String hostUser = host.getUser();
                    String hostPass = host.getPass();
                    int hostPort = host.getPort();
                    if ("telnet".equalsIgnoreCase(hostProtocal)) {
                        connection = new TelnetWrapper();
                        connection.connect(hostHost, hostPort);
                        if (hostUser != null && hostPass != null && hostUser.length() > 0 && hostPass.length() > 0) {
                            connection.send(hostUser + "\n");
                            connection.send(hostPass + "\n");
                        }
                    } else if ("ssh".equalsIgnoreCase(hostProtocal)) {
                        connection = new SshWrapper();
                        connection.connect(hostHost, hostPort);
                        connection.login(hostUser, hostPass);
                        connection.send("" + "\n");
                    }
                    connected = true;
                    while (true) {
                        int n = connection.read(b);
                        if (n > 0) {
                            String fullString = new String(b, 0, n, "ISO8859-1");
                            ((vt320) buffer).putString(fullString, host.getEncoding());
                            redraw();
                        } else if (n < 0) {
                            break;
                        }
                    }
                    nodifyParent(null);
                } catch (Exception e) {
                    nodifyParent(e);
                }
            }
        }).start();
    }
}
