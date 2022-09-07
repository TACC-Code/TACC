class BackupThread extends Thread {
        public void process(Socket socket) {
            OutputStream bos = null;
            ObjectInputStream ois = null;
            try {
                int bufferSize = socket.getSendBufferSize();
                socket.setSendBufferSize(socket_buffer_size);
                if (log.isDebugEnabled()) log.debug("Running on " + Thread.currentThread() + ". Accepted request for state transfer from " + socket.getInetAddress() + ":" + socket.getPort() + ", original buffer size was " + bufferSize + " and was reset to " + socket.getSendBufferSize() + ", passing outputstream up... ");
                ois = new ObjectInputStream(socket.getInputStream());
                String state_id = (String) ois.readObject();
                Address stateRequester = (Address) ois.readObject();
                bos = new BufferedOutputStream(new StreamingOutputStreamWrapper(socket), socket_buffer_size);
                up_prot.up(new Event(Event.STATE_TRANSFER_OUTPUTSTREAM, new StateTransferInfo(stateRequester, bos, state_id)));
            } catch (IOException e) {
                if (log.isWarnEnabled()) {
                    log.warn("State writer socket thread spawned abnormaly", e);
                }
            } catch (ClassNotFoundException e) {
            } finally {
                if (!socket.isConnected()) {
                    if (log.isWarnEnabled()) log.warn("Could not receive connection from state receiver. Closing socket...");
                }
                Util.close(bos);
                Util.close(socket);
            }
        }
}
