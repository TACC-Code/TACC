class BackupThread extends Thread {
    @Override
    public void run() {
        while (client.isRunning() && !serverContext.isServerSaidByeBye()) {
            try {
                final String line = serverContext.getServerEndpoint().readLine();
                if (client.isRunning()) {
                    if (line != null) {
                        serverContext.getServerEndpoint().read();
                        handleMessage(line);
                    } else {
                        LOGGER.debug(String.format("server %s closed connection non-gracefully", serverContext.getServerInetAddress()));
                        serverContext.setServerSaidByeBye(true);
                        client.removeServerHandler();
                        break;
                    }
                }
            } catch (final SocketException e) {
                if (e.getMessage().equals(ERROR_SOCKET_CLOSED) || e.getMessage().equals(ERROR_RECV_FAILED) || e.getMessage().equals(ERROR_CONNECTION_RESET)) {
                    if (e.getMessage().equals(ERROR_SOCKET_CLOSED)) {
                        final String msg = "tried to read or write to closed socket %s";
                        LOGGER.error(String.format(msg, serverContext.getServerInetAddress()));
                    } else {
                        final String msg = "socket in error state [%s] for server %s";
                        LOGGER.error(String.format(msg, e.getMessage(), serverContext.getServerInetAddress()));
                    }
                    serverContext.setServerSaidByeBye(true);
                    client.removeServerHandler();
                    break;
                }
                LOGGER.error(e.getMessage(), e);
            } catch (final IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
            ServerUtil.defaultSleep();
        }
    }
}
