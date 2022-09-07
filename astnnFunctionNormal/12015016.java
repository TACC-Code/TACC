class BackupThread extends Thread {
    void processEvents() {
        if (currentRecordingTake.size() == 0 && (!stack.isEmpty())) {
            if (lastPart != null) {
                project.getEditHistoryContainer().mark(" Recording take ");
                if (!isDrumTake) {
                    detachLastTake();
                }
                project.getEditHistoryContainer().notifyEditHistoryListeners();
            }
        }
        Event e = null;
        while ((e = stack.pop()) != null) {
            ShortMessage shm = e.mess;
            long tick = e.stamp;
            if (shm.getCommand() == ShortMessage.NOTE_ON || shm.getCommand() == ShortMessage.NOTE_OFF) {
                if (shm.getCommand() == ShortMessage.NOTE_OFF || shm.getData2() == 0) {
                    NoteEvent noteEvent = pendingNoteEvents.get(shm.getChannel() << 8 | shm.getData1());
                    if (noteEvent != null) {
                        long duration = tick - noteEvent.getStartTick();
                        if (duration < 0) {
                            duration = duration + sequencer.getLoopEndPoint() - sequencer.getLoopStartPoint();
                        }
                        noteEvent.setDuration(duration);
                        pendingNoteEvents.remove(shm.getChannel() << 8 | shm.getData1());
                        addEventToRecordingTracks(noteEvent);
                    }
                } else {
                    pendingNoteEvents.put(shm.getChannel() << 8 | shm.getData1(), new NoteEvent((FrinikaTrackWrapper) null, tick, shm.getData1(), shm.getData2(), shm.getChannel(), 0));
                }
            } else if (shm.getCommand() == ShortMessage.CONTROL_CHANGE) {
                addEventToRecordingTracks(new ControllerEvent((FrinikaTrackWrapper) null, tick, shm.getData1(), shm.getData2()));
            } else if (shm.getCommand() == ShortMessage.PITCH_BEND) {
                addEventToRecordingTracks(new PitchBendEvent((FrinikaTrackWrapper) null, tick, ((shm.getData1()) | (shm.getData2() << 7)) & 0x7fff));
            }
        }
    }
}
