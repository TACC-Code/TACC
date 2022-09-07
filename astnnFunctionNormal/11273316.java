class BackupThread extends Thread {
    synchronized HandshakeStatus writeRecord(EngineOutputRecord outputRecord, EngineArgs ea, MAC writeMAC, CipherBox writeCipher) throws IOException {
        if (hasOutboundDataInternal()) {
            HandshakeStatus hss = getOutboundData(ea.netData);
            if (debug != null && Debug.isOn("packet")) {
                dumpPacket(ea, true);
            }
            return hss;
        }
        if (outboundClosed) {
            throw new IOException("The write side was already closed");
        }
        outputRecord.write(ea, writeMAC, writeCipher);
        if (debug != null && Debug.isOn("packet")) {
            dumpPacket(ea, false);
        }
        return null;
    }
}
