class BackupThread extends Thread {
    public UndoableEventTransformation(ScrEvent theEvent) {
        itsEvent = theEvent;
        undoTime = theEvent.getTime();
        undoPitch = theEvent.getPitch();
        undoVelocity = theEvent.getVelocity();
        undoDuration = theEvent.getDuration();
        undoChannel = theEvent.getChannel();
    }
}
