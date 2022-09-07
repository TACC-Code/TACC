class BackupThread extends Thread {
    public void redo() {
        undoTime = itsEvent.getTime();
        undoPitch = itsEvent.getPitch();
        undoVelocity = itsEvent.getVelocity();
        undoDuration = itsEvent.getDuration();
        undoChannel = itsEvent.getChannel();
        itsEvent.setTime(redoTime);
        itsEvent.setPitch(redoPitch);
        itsEvent.setVelocity(redoVelocity);
        itsEvent.setDuration(redoDuration);
        itsEvent.setChannel(redoChannel);
    }
}
