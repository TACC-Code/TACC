class BackupThread extends Thread {
        public BlockedThread(LogWriter writer, String name) {
            logWriter = writer;
            this.setName(name);
        }
}
