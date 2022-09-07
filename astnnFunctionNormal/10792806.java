class BackupThread extends Thread {
    protected void close() {
        if (state == WAITING && dcc.getSelectionKey() == null) {
        } else if (state == WAITING) {
            log.fine("closing peer " + getPeerAddress() + " (state = " + state + ")");
            log.fine("socket channel = " + dcc.getChannel());
            SelectionKey key = dcc.getSelectionKey();
            log.fine("interestedOps = " + ((key != null && key.isValid()) ? Integer.toHexString(key.interestOps()) : "n/a"));
            checkSendQueue(dcc);
        } else if (state == REQUESTING_PARTS) {
            Exception e = new Exception();
            log.log(Level.FINE, "closing peer " + getPeerAddress() + " (transferring)", e);
        } else if (state != REQUESTING_STARTUP) log.fine("closing peer " + getPeerAddress() + " (state = " + state + ")");
        Collection taggedGapList = taggedGaps.getGaps();
        Iterator it = taggedGapList.iterator();
        ArrayList removedgaps = new ArrayList();
        while (it.hasNext()) {
            Gap gap = (Gap) it.next();
            log.finest(dcc.getConnectionNumber() + " undo tagging for gap: " + gap.getStart() + "-" + gap.getEnd());
            currentDownload.getUntaggedGaps().addGap(gap.getStart(), gap.getEnd());
            removedgaps.add(gap);
        }
        while (!removedgaps.isEmpty()) {
            Gap gap = (Gap) removedgaps.remove(removedgaps.size() - 1);
            taggedGaps.removeGap(gap.getStart(), gap.getEnd());
        }
        closed = true;
        dcc.source.setActive(false);
        if (dcc.isConnected()) {
            dcc.source.setNextRetry();
        } else {
            dcc.source.setNextRetry(System.currentTimeMillis() + dContext.getSourceRetryInterval() * 1000);
        }
    }
}
