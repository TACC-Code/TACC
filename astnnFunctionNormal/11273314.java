class BackupThread extends Thread {
    synchronized void writeRecord(EngineOutputRecord outputRecord, MAC writeMAC, CipherBox writeCipher) throws IOException {
        if (outboundClosed) {
            throw new IOException("writer side was already closed.");
        }
        outputRecord.write(writeMAC, writeCipher);
        if (outputRecord.isFinishedMsg()) {
            outboundList.addLast(HandshakeStatus.FINISHED);
        }
    }
}
