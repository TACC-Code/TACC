class BackupThread extends Thread {
        public void run() {
            try {
                BufferedReader bRead = ((ClientHub) clients.get(socket)).getChannelRead();
                thisThread = Thread.currentThread();
                String strReceive = null;
                while (infoHub.isOnline() && thisThread == Thread.currentThread()) {
                    if ((strReceive = bRead.readLine()) != null && strReceive.length() > 0) {
                        if (strReceive.equalsIgnoreCase(infoHub.getCommClientDisconnected())) {
                            terminateClient(socket);
                            thisThread = null;
                        } else if (strReceive.startsWith(infoHub.getCommPrivateMessage())) {
                            String msg = infoHub.getCommPrivateMessageOf() + ((ClientHub) clients.get(socket)).getUsername() + strReceive.substring(strReceive.indexOf(" ", infoHub.getCommPrivateMessage().length() + 1));
                            sendMessage(msg, (Socket) usernames.get(strReceive.split(" ")[1]));
                        } else {
                            sendMessage("[" + ((ClientHub) clients.get(socket)).getUsername() + "] " + strReceive, null);
                        }
                    }
                }
            } catch (IOException e) {
            }
        }
}
