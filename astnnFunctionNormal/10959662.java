class BackupThread extends Thread {
        public void run() {
            this.setName(this.getName() + "-SendMessageThread");
            if (logger.isDebugEnabled()) logger.debug("Send Message Thread is Started: ");
            try {
                Socket socketSender = sockets.get(this.threadUserConnection);
                if (socketSender == null) this.interrupt();
                ObjectOutput socketSenderObjectOutputStream = new ObjectOutputStream(socketSender.getOutputStream());
                socketSenderObjectOutputStream.writeObject(this.threadMessage);
                socketSenderObjectOutputStream.flush();
                socketSenderObjectOutputStream.close();
                if (logger.isDebugEnabled()) logger.debug("Message is Sent to: " + socketSender.getInetAddress().getHostAddress() + " Message: " + this.threadMessage);
            } catch (Exception e) {
                logger.warn("Can`t send message to peer: ", e);
                this.interrupt();
            }
        }
}
