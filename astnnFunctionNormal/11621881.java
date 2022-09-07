class BackupThread extends Thread {
    protected STBPGameEngineProxy initConnection(ProtocolReader reader, ProtocolWriter writer) {
        return new STBPGameEngineProxy(reader, writer);
    }
}
