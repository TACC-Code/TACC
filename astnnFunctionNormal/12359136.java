class BackupThread extends Thread {
    private void overFlowAndTest(int readCursor, int writeCursor, int nextReadCSeq, int nextWriteCseq, int writeCSeqShift, int numberOfPacketsToWrite) {
        int localNextWriteCSeq = nextWriteCseq + writeCSeqShift;
        int localNumberPacketsToWrite = numberOfPacketsToWrite;
        int localNextReadCSeq = (localNextWriteCSeq + 1) - 100;
        for (; localNumberPacketsToWrite > 0; localNumberPacketsToWrite--) {
            this.jitterBuffer.write(createBuffer(localNextWriteCSeq));
            localNextWriteCSeq -= 2;
        }
        localNumberPacketsToWrite = numberOfPacketsToWrite;
        int tStamp = localNextReadCSeq * 20;
        int desiredCSeq = (nextWriteCseq + writeCSeqShift) - 2;
        boolean reasonToLive = true;
        localNextWriteCSeq = nextWriteCseq + writeCSeqShift;
        while (reasonToLive) {
            RtpPacket p = this.jitterBuffer.read(tStamp);
            RtpPacket patternPacket = createPatternPacket(localNextReadCSeq);
            createPatternPacket(localNextReadCSeq);
            try {
                if (p.getDuration() != 20) {
                    patternPacket.setDuration(p.getDuration());
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                throw e;
            }
            makeAssertionTestRtpPacket(patternPacket, p);
            tStamp += p.getDuration();
            if (desiredCSeq == p.getSeqNumber()) {
                return;
            } else {
                if (localNextReadCSeq >= nextWriteCseq - 1) {
                    localNumberPacketsToWrite--;
                    localNextReadCSeq = (localNextWriteCSeq - 2 * localNumberPacketsToWrite);
                    if (localNumberPacketsToWrite == 0) {
                        return;
                    }
                } else {
                    localNextReadCSeq++;
                }
            }
        }
    }
}
