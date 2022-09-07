class BackupThread extends Thread {
    public void run() {
        if (!infoHub.isOnline()) {
            try {
                serverSocket = new ServerSocket(infoHub.getServerPort());
                infoHub.setOnline();
                daDTC.pushTerminalLog("Server has been created. Listening for new connections...");
            } catch (Exception e) {
            }
        }
        while (infoHub.isOnline()) {
            try {
                Socket socket = serverSocket.accept();
                clients.put(socket, new ClientHub(socket));
                BufferedWriter bWrite = ((ClientHub) clients.get(socket)).getChannelWrite();
                BufferedReader bRead = ((ClientHub) clients.get(socket)).getChannelRead();
                String user = bRead.readLine().substring(infoHub.getCommConnect().length() + 1);
                if (user != null && user.length() > 0 && !usernames.containsKey(user)) {
                    daDTC.pushTerminalLog("Incomming client connection request...");
                    clientConnections++;
                    ((ClientHub) clients.get(socket)).setUsername(user);
                    usernames.put(user, socket);
                    daDTC.pushTerminalLog("Looking up hostname for client " + user + "...");
                    daDTC.pushTerminalLog("Client " + user + " (" + socket.getInetAddress().getHostName() + ") has connected.");
                    daDTC.setStatusConnected();
                    bWrite.write(infoHub.getCommConnectionAccepted() + " " + user);
                    bWrite.newLine();
                    bWrite.flush();
                    new ThreadControlMessages(socket).start();
                } else {
                    bWrite.write(infoHub.getCommUserExistance());
                    bWrite.newLine();
                    bWrite.flush();
                    clients.remove(socket);
                    socket.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
