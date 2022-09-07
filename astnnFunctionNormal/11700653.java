class BackupThread extends Thread {
    public int processRun(ProcessingThread proc) throws IOException {
        final RenderContext rc = (RenderContext) proc.getClientArg("context");
        final ConsumerContext consc = (ConsumerContext) rc.getOption(KEY_CONSC);
        final RenderSource source = (RenderSource) proc.getClientArg("source");
        final AudioTrail at = consc.doc.getAudioTrail();
        final int inTrnsLen = ((Integer) proc.getClientArg("inTrnsLen")).intValue();
        final RandomAccessRequester rar = (RandomAccessRequester) proc.getClientArg("rar");
        final boolean randomAccess = rar != null;
        int readLen, writeLen;
        long readOffset, remainingRead;
        boolean consStarted = false;
        boolean consFinished = false;
        final int inOff = 0;
        remainingRead = context.getTimeSpan().getLength();
        if (source.validAudio) ProcessingThread.setNextProgStop(0.9f);
        readOffset = context.getTimeSpan().getStart();
        try {
            prodLp: while (!ProcessingThread.shouldCancel()) {
                if (randomAccess) {
                    source.blockSpan = rar.getNextSpan();
                    readLen = (int) source.blockSpan.getLength();
                } else {
                    readLen = (int) Math.min(inTrnsLen - inOff, remainingRead);
                    source.blockSpan = new Span(readOffset, readOffset + readLen);
                    remainingRead -= readLen;
                    readOffset += readLen;
                }
                if (readLen == 0) break prodLp;
                writeLen = readLen;
                source.audioBlockBufLen = writeLen;
                at.readFrames(consc.inBuf, inOff, source.blockSpan);
                for (int ch = 0; ch < source.numAudioChannels; ch++) {
                    System.arraycopy(consc.inBuf[ch], inOff, source.audioBlockBuf[ch], 0, writeLen);
                }
                if (ProcessingThread.shouldCancel()) break prodLp;
                if (!invokeProducerRender(proc, source, plugIn)) return FAILED;
            }
            consFinished = true;
            if (ProcessingThread.shouldCancel()) {
                invokeProducerCancel(proc, source, plugIn);
                return CANCELLED;
            } else {
                return (invokeProducerFinish(proc, source, plugIn) ? DONE : FAILED);
            }
        } finally {
            if (consStarted && !consFinished) {
                try {
                    invokeProducerCancel(proc, source, plugIn);
                } catch (IOException e2) {
                    proc.setException(e2);
                }
            }
            if (source.markers != null) {
                source.markers.dispose();
                source.markers = null;
            }
        }
    }
}
