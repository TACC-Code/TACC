class BackupThread extends Thread {
    @Override
    public void handleQxEvent(Event event) {
        if (event.getKind().equals(EVENT_KIND.TX_CMD_ADDED)) {
            if (event.getCmd() instanceof TcpSendCmd) {
                Socket socket = (Socket) event.getQx().getEngine().getPort().getChannel();
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(socket.getOutputStream(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String cmd = new String(((TcpSendCmd) event.getCmd()).getStream());
                if (Platform.inDebugMode()) {
                    System.out.println("Send TCP command to [" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort() + "] : '" + cmd + "'");
                }
                pw.println(cmd);
            }
        }
    }
}
