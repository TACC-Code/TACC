class BackupThread extends Thread {
    private static void readMessageFromNetwork(SelectionKey key) {
        NetworkClient distant = (NetworkClient) key.attachment();
        ByteBuffer byteBuffer = ByteBuffer.allocate(500);
        if (distant.msgbuffer != null) {
            byteBuffer.put(distant.msgbuffer);
            distant.msgbuffer = null;
        }
        if (distant.isToBeKilled) {
            return;
        }
        int readsize;
        try {
            readsize = distant.getChannel().read(byteBuffer);
            if (readsize == -1) {
                throw new java.io.IOException();
            }
        } catch (IOException e) {
            Main.preferences.LOGGER.fine("In NonBlockingServer::ReadMessageFromNetwork -> killing a client!");
            distant.isToBeKilled = true;
            try {
                distant.hisSocket.close();
            } catch (IOException e2) {
                Main.preferences.LOGGER.fine("In NonBlockingServer::ReadMessageFromNetWork ->" + "can't close socket!");
            }
            return;
        }
        processBuffer(byteBuffer, distant, readsize);
    }
}
