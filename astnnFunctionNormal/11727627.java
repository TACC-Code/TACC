class BackupThread extends Thread {
    @Override
    public void deactivateChannel(HubChannel channel, HubPort port) {
        int portAsInt = port.getValue();
        logger.debug("Hub: De-activating 1-Wire hub channel " + channel.getChannel() + ":" + port);
        try {
            byte[] state = owSwitch.get(channel).readDevice();
            owSwitch.get(channel).setLatchState(portAsInt, false, false, state);
            owSwitch.get(channel).writeDevice(state);
        } catch (OneWireException e) {
            logger.error("Error de-activating hub channel: " + e.getMessage(), e);
        }
    }
}
