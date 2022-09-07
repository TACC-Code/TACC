class BackupThread extends Thread {
        public void run() {
            try {
                while (!stop) {
                    try {
                        LOG.debug("Waiting for incoming connections ...");
                        final Socket client = this.socket.accept();
                        LOG.info("Got connection from host: " + client.getInetAddress());
                        final BufferedReader reader = ServerUtil.getReader(client);
                        final BufferedWriter writer = ServerUtil.getWriter(client);
                        this.communicate(client, reader, writer);
                    } catch (IOException e) {
                        if (this.stop == true) {
                            LOG.info("Shutting down server invoked (Exception ignored).");
                        } else {
                            LOG.error("Error while client communication", e);
                        }
                    }
                }
            } finally {
                LOG.info("Ending service.");
            }
        }
}
