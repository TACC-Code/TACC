class BackupThread extends Thread {
    public static String getChannel() {
        return SyncContext.getInstance().getServerSource();
    }
}
