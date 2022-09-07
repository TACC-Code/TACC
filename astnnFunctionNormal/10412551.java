class BackupThread extends Thread {
    public void eventReceived(IRCEvent event) {
        if (event instanceof CTCPMessageEvent) {
            CTCPMessageEvent ctcpMessage = (CTCPMessageEvent) event;
            System.out.println("* Received CTCP " + ctcpMessage.getMessage() + " from " + ctcpMessage.getSender());
        }
        if (event instanceof ChannelMessageEvent) {
            ChannelMessageEvent channelMessage = (ChannelMessageEvent) event;
            System.out.println(channelMessage.getChannel() + ": <" + channelMessage.getSender() + "> " + channelMessage.getMessage());
        }
        if (event instanceof InviteEvent) {
            InviteEvent invite = (InviteEvent) event;
            System.out.println("* " + invite.getUser() + " has invited you to " + invite.getChannel());
        }
        if (event instanceof JoinEvent) {
            JoinEvent join = (JoinEvent) event;
            System.out.println("* " + join.getUser() + " has joined channel " + join.getChannel());
        }
        if (event instanceof MOTDEvent) {
            MOTDEvent motd = (MOTDEvent) event;
            System.out.println(motd.getMessage());
        }
        if (event instanceof NoticeEvent) {
            NoticeEvent notice = (NoticeEvent) event;
            System.out.println("-" + notice.getSender() + "- " + notice.getMessage());
        }
        if (event instanceof PartEvent) {
            PartEvent part = (PartEvent) event;
            System.out.println("* " + part.getUser() + " has parted " + part.getChannel());
        }
        if (event instanceof PrivateMessageEvent) {
            PrivateMessageEvent privateMessage = (PrivateMessageEvent) event;
            System.out.println("<" + privateMessage.getSender() + "> " + privateMessage.getMessage());
        }
        if (event instanceof QuitEvent) {
            QuitEvent quit = (QuitEvent) event;
            System.out.println("* " + quit.getSender() + " has quit IRC.");
        }
    }
}
