class BackupThread extends Thread {
    public void perform() {
        IDisplay display = Client.instance().getClientGUI().getDesktopManager().getDisplay("main.connect");
        if (display != null) {
            display.close();
            Client.instance().getClientGUI().getDesktopManager().removeDisplay(display);
        }
        double channelNumber = appContext.getChannelNumber();
        ClientTransceiver clientTransceiver = new ClientTransceiver(channelNumber);
        clientTransceiver.addReceiver(channelNumber);
        RegisterNewUserServerAction registerNewUserServerAction = new RegisterNewUserServerAction(nickname, email);
        registerNewUserServerAction.setTransceiver(clientTransceiver);
        ActionTools.sendToServer(registerNewUserServerAction);
    }
}
