class BackupThread extends Thread {
    public void write(String level, long thread_id, String description, String details) {
        for (int t = 0; t < logs.size(); t++) {
            ((Logging) logs.get(t)).write(level, thread_id, description, details);
        }
    }
}
