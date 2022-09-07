class BackupThread extends Thread {
    @PostLoad
    public void postQuery() {
        if (recordedProgramKey.getChannelId() >= 0 && recordedProgramKey.getStartTime() != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHmmss");
            key = Integer.toString(recordedProgramKey.getChannelId()) + "-" + formatter.format(recordedProgramKey.getStartTime());
        }
    }
}
