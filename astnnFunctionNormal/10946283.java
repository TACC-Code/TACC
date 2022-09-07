class BackupThread extends Thread {
    public void undo() {
        redoTime = itsEvent.getTime();
        redoPitch = itsEvent.getPitch();
        redoVelocity = itsEvent.getVelocity();
        redoDuration = itsEvent.getDuration();
        redoChannel = itsEvent.getChannel();
        itsEvent.setTime(undoTime);
        itsEvent.setPitch(undoPitch);
        itsEvent.setVelocity(undoVelocity);
        itsEvent.setDuration(undoDuration);
        itsEvent.setChannel(undoChannel);
    }
}
