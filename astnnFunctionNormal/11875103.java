class BackupThread extends Thread {
    public boolean performFromChannelMessage(String channel, String nick, String msg) {
        if (!msg.startsWith("\1")) return false;
        msg = msg.substring(1);
        msg = msg.substring(0, msg.length() - 1);
        String cmd = "";
        String param = "";
        int pos = msg.indexOf(' ');
        if (pos == -1) {
            cmd = msg.toLowerCase(java.util.Locale.ENGLISH);
        } else {
            cmd = msg.substring(0, pos).toLowerCase(java.util.Locale.ENGLISH);
            param = msg.substring(pos + 1);
        }
        if (cmd.equals("action")) {
            Channel c = _server.getChannel(channel, false);
            if (c != null) c.action(nick, param);
        }
        return true;
    }
}
