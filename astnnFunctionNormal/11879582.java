class BackupThread extends Thread {
            public void run() {
                DebugUtils.println("Starting ExternalCommunications Server at port: " + serverPort);
                try {
                    socket = new ServerSocket(serverPort);
                    while (true) {
                        final Socket incoming = socket.accept();
                        final BufferedReader readerIn = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
                        final PrintStream writerOut = new PrintStream(incoming.getOutputStream());
                        if ((maxNumClients > 0) && (clients.size() == maxNumClients)) {
                            ExternalClient removedClient = clients.remove(0);
                            removedClient.exitClient();
                        }
                        clients.add(new ExternalClient(ExternalCommunicationServer.this, clientID++, incoming, readerIn, writerOut));
                    }
                } catch (SocketException e) {
                    DebugUtils.println("Server Socket was Closed");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DebugUtils.println("Stopping Flash Communications Server");
            }
}
