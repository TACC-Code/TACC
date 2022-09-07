class BackupThread extends Thread {
    public Set<String> getChannels() {
        return chRecorder.keySet();
    }
}
