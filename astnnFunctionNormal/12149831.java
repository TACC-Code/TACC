class BackupThread extends Thread {
    private void aufraeumen() {
        for (String a : users) {
            ircServer.handleEvent(new QuitUserEventParameter(a).getEvent());
        }
        for (String c : channels) {
            ReturnValue<IRCChannelBean> chan = zustandsabfrage.getChannel(c);
            if (chan.getValue() != null) {
                System.out.println("found chan -> will remove it");
                if (!reperatur.removeChannel(c)) System.out.println(" didnt work... :(");
            }
        }
    }
}
