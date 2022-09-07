class BackupThread extends Thread {
    @Override
    public final void activateChannel(HubChannel channel, HubPort port) {
        int portAsInt = port.getValue();
        logger.debug("Hub: Activating 1-Wire hub channel " + channel.getChannel() + ":" + port);
        try {
            byte[] state = owSwitch.get(channel).readDevice();
            owSwitch.get(channel).setLatchState(portAsInt, true, false, state);
            owSwitch.get(channel).writeDevice(state);
        } catch (OneWireException e) {
            logger.error("Error activating hub channel: " + e.getMessage(), e);
        }
    }
}
