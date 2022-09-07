class BackupThread extends Thread {
    @Override
    public HubChannel getChannel(String couplerAddr) {
        for (HubChannel channel : this.owSwitch.keySet()) {
            OneWireContainer1F container = this.owSwitch.get(channel);
            if (container.getAddressAsString().equalsIgnoreCase(couplerAddr)) {
                return channel;
            }
        }
        return null;
    }
}
