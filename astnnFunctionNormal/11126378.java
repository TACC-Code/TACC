class BackupThread extends Thread {
    @Override
    public void write2Buffer(AbstractPacketBuffer mysqlpacketBuffer) throws UnsupportedEncodingException {
        super.write2Buffer(mysqlpacketBuffer);
        MysqlPacketBuffer buffer = (MysqlPacketBuffer) mysqlpacketBuffer;
        buffer.writeByte(protocolVersion);
        buffer.writeString(serverVersion, CODE_PAGE_1252);
        buffer.writeLong(threadId);
        buffer.writeString(seed);
        buffer.writeInt(serverCapabilities);
        buffer.writeByte(serverCharsetIndex);
        buffer.writeInt(serverStatus);
        buffer.writeBytesNoNull(new byte[13]);
        buffer.writeString(restOfScrambleBuff);
    }
}
