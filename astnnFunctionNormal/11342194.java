class BackupThread extends Thread {
    public void sendMessage(String msg, Socket socket) {
        synchronized (clients) {
            BufferedWriter bWrite = null;
            try {
                if (socket == null) {
                    Enumeration allClients = clients.elements();
                    while (allClients.hasMoreElements()) {
                        bWrite = ((ClientHub) allClients.nextElement()).getChannelWrite();
                        bWrite.write(msg);
                        bWrite.newLine();
                        bWrite.flush();
                    }
                } else {
                    bWrite = ((ClientHub) clients.get(socket)).getChannelWrite();
                    bWrite.write(msg);
                    bWrite.newLine();
                    bWrite.flush();
                }
            } catch (IOException e) {
            }
        }
    }
}
