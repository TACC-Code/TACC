class BackupThread extends Thread {
    @Override
    public void run() {
        stillNeedsRun = false;
        if (this.socket == null) {
            cleanup();
            return;
        }
        String fromServer = null;
        boolean done = false;
        boolean forcedLogout = false;
        try {
            while (!done && (fromServer = this.in.readLine()) != null) {
                String[] tokens = fromServer.split(sep, -1);
                String command = tokens[0];
                if (fromServer.startsWith("ACK: ")) {
                    command = tokens[0].substring(5);
                    handleAckNack(command, tokens);
                } else if (fromServer.startsWith("NACK: ")) {
                    command = tokens[0].substring(6);
                    handleAckNack(command, tokens);
                } else if (fromServer.equals(IWebClient.connectionClosed)) {
                    done = true;
                } else if (fromServer.equals(IWebClient.forcedLogout)) {
                    forcedLogout = true;
                    done = true;
                } else if (command.equals(IWebClient.gameInfo)) {
                    HashMap<String, GameInfo> gameHash = webClient.getGameHash();
                    GameInfo gi = GameInfo.fromString(tokens, gameHash);
                    webClient.gameInfo(gi);
                } else if (command.equals(IWebClient.userInfo)) {
                    int loggedin = Integer.parseInt(tokens[1]);
                    int enrolled = Integer.parseInt(tokens[2]);
                    int playing = Integer.parseInt(tokens[3]);
                    int dead = Integer.parseInt(tokens[4]);
                    long ago = Long.parseLong(tokens[5]);
                    String text = tokens[6];
                    webClient.userInfo(loggedin, enrolled, playing, dead, ago, text);
                } else if (command.equals(IWebClient.didEnroll)) {
                    String gameId = tokens[1];
                    String user = tokens[2];
                    webClient.didEnroll(gameId, user);
                } else if (command.equals(IWebClient.didUnenroll)) {
                    String gameId = tokens[1];
                    String user = tokens[2];
                    webClient.didUnenroll(gameId, user);
                } else if (command.equals(IWebClient.gameCancelled)) {
                    String gameId = tokens[1];
                    String byUser = tokens[2];
                    webClient.gameCancelled(gameId, byUser);
                } else if (command.equals(IWebClient.gameStartsNow)) {
                    String gameId = tokens[1];
                    int port = Integer.parseInt(tokens[2]);
                    webClient.gameStartsNow(gameId, port);
                } else if (command.equals(IWebClient.gameStartsSoon)) {
                    String gameId = tokens[1];
                    webClient.gameStartsSoon(gameId);
                } else if (command.equals(IWebClient.gameStartsSoon)) {
                    String gameId = tokens[1];
                    webClient.gameStarted(gameId);
                } else if (command.equals(IWebClient.chatDeliver)) {
                    String chatId = tokens[1];
                    long when = Long.parseLong(tokens[2]);
                    String sender = tokens[3];
                    String message = tokens[4];
                    boolean resent = Boolean.valueOf(tokens[5]).booleanValue();
                    webClient.chatDeliver(chatId, when, sender, message, resent);
                } else if (command.equals(IWebClient.grantAdmin)) {
                    webClient.grantAdminStatus();
                } else {
                    if (webClient != null) {
                        webClient.showAnswer(fromServer);
                    }
                }
            }
            writeLog("End of SocketClientThread while loop, done = " + done + " readline " + (fromServer == null ? " null " : "'" + fromServer + "'"));
            if (loggedIn) {
                webClient.connectionReset(forcedLogout);
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "WebClientSocketThread IOException!");
            webClient.connectionReset(false);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "WebClientSocketThread whatever Exception!", e);
            Thread.dumpStack();
            webClient.connectionReset(false);
        }
        cleanup();
    }
}
