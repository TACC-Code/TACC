class BackupThread extends Thread {
    private void writeTrack(TGTrack track) throws IOException {
        GMChannelRoute channel = getChannelRoute(track.getChannelId());
        int flags = 0;
        if (isPercussionChannel(track.getSong(), track.getChannelId())) {
            flags |= 0x01;
        }
        writeUnsignedByte(flags);
        writeStringByte(track.getName(), 40);
        writeInt(track.getStrings().size());
        for (int i = 0; i < 7; i++) {
            int value = 0;
            if (track.getStrings().size() > i) {
                TGString string = (TGString) track.getStrings().get(i);
                value = string.getValue();
            }
            writeInt(value);
        }
        writeInt(1);
        writeInt(channel.getChannel1() + 1);
        writeInt(channel.getChannel2() + 1);
        writeInt(24);
        writeInt(Math.min(Math.max(track.getOffset(), 0), 12));
        writeColor(track.getColor());
    }
}
