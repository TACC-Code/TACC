class BackupThread extends Thread {
    public synchronized TvControllerResult synchronizeRecordingInfo() {
        timer.clear(new Timer.Condition() {

            public boolean check(Timer.Task task) {
                return task instanceof RecordingTask;
            }
        });
        SortedSet<RecordingData> datas = recordingInfo.getFuture(System.currentTimeMillis());
        for (RecordingData data : datas) {
            RecordingTask task = new RecordingTask(data.getStart(), data.getEnd(), data.getChannel(), data.getName());
            timer.schedule(task);
        }
        TvControllerEvent event = new TvControllerEvent(TvControllerEvent.Type.SYNCHRONIZED_RECORDINGS, "synchronizeRecordingInfo: synchronized recordings");
        event.addProperty("recordingCount", datas.size());
        observers.event(event);
        return new TvControllerResult(TvController.Operation.SYNCHRONIZE_RECORDING_INFO, true, datas.size() + " recordings");
    }
}
