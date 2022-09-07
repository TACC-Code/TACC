class BackupThread extends Thread {
    public void copyToSchedule(ScheduleItem to) throws Exception {
        Calendar start = Calendar.getInstance();
        start.setTime(this.getStart());
        to.setStart(start);
        to.setDuration(this.getDuration());
        to.setCapType(this.getCapType());
        to.setType(this.getType());
        to.setPostTask(this.getPostTask());
        to.setChannel(this.getChannel());
        to.setFilePattern(this.getFilePattern());
        to.setName(this.getName());
        to.setCreatedFrom(this.getCreatedFrom());
        to.setKeepFor(this.getKeepFor());
        to.setAutoDeletable(this.isAutoDeletable());
        to.setState(this.getState());
        to.setStatus(this.getStatus());
        to.setCapturePathIndex(this.getCapturePathIndex());
        if (this.isAborted()) to.abort();
    }
}
