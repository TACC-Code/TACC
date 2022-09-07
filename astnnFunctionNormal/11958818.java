class BackupThread extends Thread {
    public void run() {
        while (steps-- != 0) {
            memory.write(0, memory.read(0));
            timeline.sleep(1);
        }
        memory.flush();
    }
}
