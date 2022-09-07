class BackupThread extends Thread {
    public static void debug(PrintWriter writer, String name, LockInfo info) {
        writer.println("Thread: " + name);
        if (info != null) {
            writer.format("%20s: %s\n", "Lock type", info.getLockType());
            writer.format("%20s: %s\n", "Lock mode", info.getLockMode());
            writer.format("%20s: %s\n", "Lock id", info.getId());
            writer.format("%20s: %s\n", "Held by", Arrays.toString(info.getOwners()));
            writer.format("%20s: %s\n", "Held by", Arrays.toString(info.getOwners()));
            writer.format("%20s: %s\n", "Held by", Arrays.toString(info.getOwners()));
            writer.format("%20s: %s\n", "Waiting for read", Arrays.toString(info.getWaitingForRead()));
            writer.format("%20s: %s\n\n", "Waiting for write", Arrays.toString(info.getWaitingForWrite()));
        }
    }
}
