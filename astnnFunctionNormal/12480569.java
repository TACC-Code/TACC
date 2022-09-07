class BackupThread extends Thread {
    public static void debug(PrintWriter writer) {
        writer.println("Threads currently waiting for a lock:");
        writer.println("=====================================");
        Map<String, LockInfo> threads = getWaitingThreads();
        for (Map.Entry<String, LockInfo> entry : threads.entrySet()) {
            debug(writer, entry.getKey().toString(), entry.getValue());
        }
    }
}
