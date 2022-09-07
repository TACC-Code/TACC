class BackupThread extends Thread {
    public FmsChannel getChannel(String jobname) {
        if (null != info) {
            return info.channelInfo.getChannel(jobname);
        }
        return null;
    }
}
