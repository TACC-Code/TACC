class BackupThread extends Thread {
        private void communicate(Socket clientSocket, BufferedReader reader, BufferedWriter writer) throws IOException {
            try {
                final boolean accept = this.receiver.acceptTransmission(clientSocket.getInetAddress().getHostAddress());
                final String msgAccept = accept ? CommunicationConstants.CONNECT_ACCEPTED : CommunicationConstants.CONNECT_NOT_ACCEPTED;
                LOG.debug("communicating: sending permission message '" + msgAccept + "'.");
                writer.write(msgAccept + "\n");
                writer.flush();
                LOG.debug("communicating: permission message sent.");
                if (accept == false) {
                    return;
                }
                LOG.debug("communicating: waiting for version ...");
                final int version = Integer.parseInt(reader.readLine());
                LOG.debug("communicating: got version " + version);
                if (version != Movie.DATA_VERSION) {
                    writer.write(CommunicationConstants.VERSION_NOT_OKAY + "\n");
                    writer.flush();
                    return;
                }
                LOG.debug("communicating: writing ok.");
                writer.write(CommunicationConstants.VERSION_OKAY + "\n");
                writer.flush();
                ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream());
                VersionedMovies versionedMovies;
                try {
                    versionedMovies = (VersionedMovies) objectInput.readObject();
                } catch (ClassNotFoundException e) {
                    LOG.fatal("Could not read object.", e);
                    throw new RuntimeException(e);
                }
                LOG.debug("Forwarding received versioned movies: " + versionedMovies);
                this.receiver.dataReceived(versionedMovies);
                LOG.debug("communicating: everything was fine, closing connection.");
            } finally {
                LOG.debug("communicating: closing reader, writer and socket.");
                ServerUtil.closeReader(reader);
                ServerUtil.closeWriter(writer);
                ServerUtil.closeSocket(clientSocket);
            }
            LOG.debug("communicating: end of communication.");
        }
}
