class BackupThread extends Thread {
    public synchronized void logStats(Logger log, long totalTimeInMs) {
        boolean infoLevel = totalTimeInMs > Constants.LONG_OPERATION_THRESHOLD;
        Set<String> keys = new TreeSet<String>(stats.keySet());
        StringBuilder statsPrintout = new StringBuilder(channel.getChannelId());
        for (String key : keys) {
            statsPrintout.append(", " + key + "=" + stats.get(key));
        }
        if (infoLevel) {
            log.info("Routing {}", statsPrintout);
        } else {
            log.debug("Routing {}", statsPrintout);
        }
    }
}
