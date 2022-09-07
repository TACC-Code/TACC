class BackupThread extends Thread {
    @Override
    public boolean requiresStatusByte(MidiEvent prevEvent) {
        if (prevEvent == null) {
            return true;
        }
        if (!(prevEvent instanceof ChannelEvent)) {
            return true;
        }
        ChannelEvent ce = (ChannelEvent) prevEvent;
        return !(mType == ce.getType() && mChannel == ce.getChannel());
    }
}
