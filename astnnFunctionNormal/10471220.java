class BackupThread extends Thread {
    @Override
    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        int length = (int) slea.available();
        if (length == 6) {
            mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
            mplew.writeInt(slea.readInt());
            mplew.writeShort(slea.readShort());
            c.getSession().write(mplew.getPacket());
        } else if (length > 6) {
            byte[] bytes = slea.read(length - 9);
            mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
            mplew.write(bytes);
            c.getSession().write(mplew.getPacket());
        }
    }
}
