class BackupThread extends Thread {
    @Override
    protected void writeImpl() {
        if (_channel == null) return;
        writeC(0xfe);
        writeH(0x31);
        writeS(_channel.getChannelLeader().getName());
        writeD(0);
        writeD(_channel.getMemberCount());
        writeD(_channel.getPartys().size());
        for (L2Party p : _channel.getPartys()) {
            writeS(p.getLeader().getName());
            writeD(p.getPartyLeaderOID());
            writeD(p.getMemberCount());
        }
    }
}
