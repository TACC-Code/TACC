class BackupThread extends Thread {
    public void run() {
        PacketFilter filter;
        PacketCollector collector;
        Packet packet;
        Message message;
        String sender, text;
        try {
            connection = new XMPPConnection(server);
            connection.connect();
            connection.login(username, password, RESOURCE);
            queue.enqueue(new ConnectEvent(ConnectEvent.JABBER));
            filter = new PacketFilter() {

                public boolean accept(Packet packet) {
                    return true;
                }
            };
            collector = connection.createPacketCollector(filter);
            while (true) {
                packet = collector.nextResult();
                if (!(packet instanceof Message)) {
                    System.out.println("ignored: " + packet);
                    continue;
                }
                message = (Message) packet;
                sender = message.getFrom();
                if (sender == null) {
                    System.out.println("ignored: " + packet);
                    continue;
                }
                text = message.getBody();
                if (text == null) continue;
                if ((opponent == null) || !sender.equalsIgnoreCase(opponent)) {
                    if (!(text.equals(CONNECT_MESSAGE) || text.equals(GOODBYE_MESSAGE))) {
                        System.out.println("ignored: " + sender + ": " + text);
                        continue;
                    }
                }
                MessageEvent event = new MessageEvent(sender, text);
                queue.enqueue(event);
            }
        } catch (Exception exception) {
            System.out.println("exception: " + exception);
            disconnect(exception);
        }
        disconnect();
    }
}
