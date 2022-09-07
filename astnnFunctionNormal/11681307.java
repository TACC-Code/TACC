class BackupThread extends Thread {
    private void initializeChannels() {
        RegisteredHardware hardware = (RegisteredHardware) getUserObject();
        if (hardware instanceof HomenetHardware) {
            for (int channel = 0; channel < ((HomenetHardware) hardware).getNumChannels(); channel++) {
                add(new ChannelNode(((HomenetHardware) hardware).getChannel(channel), treeView, serverContext));
            }
        }
    }
}
