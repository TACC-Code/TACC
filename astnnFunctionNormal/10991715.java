class BackupThread extends Thread {
    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        Channel channel = m.getChannelParameter(0);
        String password = null;
        if (m.getParameterCount() >= 2) {
            password = m.getParameter(1);
        }
        if (channel == null) {
            if (client.getUser().getAccessLevel() >= AccessLevel.OPERATOR) {
                ChannelConfig config = new ChannelConfig();
                config.setSettings(new Settings());
                config.setName(m.getParameter(0).replaceFirst("#", ""));
                config.setDescription("");
                channel = ChannelManager.getInstance().createChannel(config);
                PlineMessage response = new PlineMessage();
                response.setKey("command.join.created", m.getParameter(0));
                client.send(response);
            } else {
                PlineMessage response = new PlineMessage();
                response.setKey("command.join.unknown", m.getParameter(0));
                client.send(response);
            }
        }
        if (channel != null) {
            ChannelConfig channelConfig = channel.getConfig();
            if (client.getUser().getAccessLevel() < channelConfig.getAccessLevel()) {
                PlineMessage accessDenied = new PlineMessage();
                accessDenied.setKey("command.join.denied");
                client.send(accessDenied);
            } else if (channelConfig.isPasswordProtected() && !channelConfig.getPassword().equals(password)) {
                log.severe(client.getUser().getName() + "(" + client.getInetAddress() + ") " + "attempted to join the protected channel '" + channelConfig.getName() + "'.");
                PlineMessage accessDenied = new PlineMessage();
                accessDenied.setKey("command.join.wrong_password");
                client.send(accessDenied);
            } else if (channel.isFull() && client.getUser().isPlayer()) {
                PlineMessage channelfull = new PlineMessage();
                channelfull.setKey("command.join.full");
                client.send(channelfull);
            } else if (client.getUser().isPlayer() && !channelConfig.isProtocolAccepted(client.getProtocol().getName())) {
                String type = channelConfig.getSpeed() == Speed.FAST ? "TetriFast" : "TetriNET";
                client.send(new PlineMessage("command.join.speed", type));
            } else {
                AddPlayerMessage move = new AddPlayerMessage();
                move.setClient((Client) m.getSource());
                channel.send(move);
            }
        }
    }
}
