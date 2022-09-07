class BackupThread extends Thread {
    public FlapCommand genFlapCommand(FlapPacket packet) {
        int channel = packet.getChannel();
        if (channel == LoginFlapCmd.CHANNEL_LOGIN) {
            return new LoginFlapCmd(packet);
        } else if (channel == SnacFlapCmd.CHANNEL_SNAC) {
            return new SnacFlapCmd(packet);
        } else if (channel == FlapErrorCmd.CHANNEL_ERROR) {
            return new FlapErrorCmd(packet);
        } else if (channel == CloseFlapCmd.CHANNEL_CLOSE) {
            return new CloseFlapCmd(packet);
        } else {
            return null;
        }
    }
}
