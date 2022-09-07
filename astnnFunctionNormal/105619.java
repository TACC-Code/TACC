class BackupThread extends Thread {
    public boolean equals(Object item) {
        ScheduleItem compTo = (ScheduleItem) item;
        if (compTo.getStart().getTime() == this.getStart().getTime() && compTo.getDuration() == this.getDuration() && compTo.getChannel().equalsIgnoreCase(this.getChannel())) {
            return true;
        }
        return false;
    }
}
