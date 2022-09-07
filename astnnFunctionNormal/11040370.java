class BackupThread extends Thread {
    @Override
    public void run() {
        try {
            StringBuilder receivedText = new StringBuilder(Consts.HttpClientBufferSize * 5);
            ByteBuffer receiveBuffer = ByteBuffer.allocateDirect(Consts.HttpClientBufferSize);
            while (this.started) {
                receiveBuffer.clear();
                int receivedBytesCount = this.socket.read(receiveBuffer);
                if (receivedBytesCount == -1 || receivedBytesCount == 0) {
                    break;
                }
                receiveBuffer.flip();
                receivedText.append(this.decoder.decode(receiveBuffer).toString());
                this.log.write(Level.INFO, String.format(this.messages.getString("HttpClientRequest"), receivedBytesCount, this.remoteAddress));
                int indexReq = receivedText.indexOf(Consts.HeaderCRLF + Consts.HeaderCRLF);
                while (indexReq != -1) {
                    indexReq += (Consts.HeaderCRLF + Consts.HeaderCRLF).length();
                    String textToDecode = receivedText.substring(0, indexReq);
                    receivedText = new StringBuilder(receivedText.substring(indexReq, receivedText.length()));
                    if (!textToDecode.equals("")) {
                        this.log.write(Level.FINE, String.format(this.messages.getString("HttpReceived"), textToDecode));
                        parseHttpRequest(textToDecode);
                    }
                    indexReq = receivedText.indexOf(Consts.HeaderCRLF + Consts.HeaderCRLF);
                }
            }
        } catch (AsynchronousCloseException ex) {
            this.log.write(Level.WARNING, this.messages.getString("HttpClientViolentlyDisconected"));
        } catch (IOException ex) {
            this.log.write(Level.SEVERE, String.format(this.messages.getString("HttpClientSocketException"), ex.toString()));
        } finally {
            synchronized (this.server.getClients()) {
                this.server.getClients().remove(this);
            }
            try {
                this.socket.socket().close();
                this.socket.close();
                this.log.write(Level.INFO, String.format(this.messages.getString("HttpClientClosedSocket"), this.remoteAddress));
                this.log.write(Level.INFO, this.messages.getString("ThreadStopped"));
            } catch (Exception ex) {
            }
        }
    }
}
