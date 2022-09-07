class BackupThread extends Thread {
    public void writePacket(AEPacketRaw packet) throws SpreadException {
        if (connection == null) {
            log.warning("no Spread connection");
            return;
        }
        int n = packet.getNumEvents();
        if (n == 0) return;
        ByteBuffer buf = ByteBuffer.allocate(computeByteSizeOfPacket(packet));
        int[] ts = packet.getTimestamps();
        int[] addr = packet.getAddresses();
        buf.putInt(sendSequenceNumber++);
        for (int i = 0; i < n; i++) {
            buf.putShort((short) addr[i]);
            buf.putInt(ts[i]);
        }
        SpreadMessage msg = new SpreadMessage();
        msg.addGroup("group");
        msg.setFifo();
        msg.setData(buf.array());
        connection.multicast(msg);
    }
}
