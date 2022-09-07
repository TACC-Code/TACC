class BackupThread extends Thread {
    public boolean remove(Object key, TimedConcurrentHashMap<?, ?> routingTable) {
        Link link = (Link) routingTable.get(key);
        if ((link != null) && (link.getChannel() != null) && link.getChannel().isDefaultGw() && link.getDestinationUuid().equals(key)) {
            return false;
        }
        for (String alias : aliasTable.keySet()) {
            if (aliasTable.get(alias).equals(key)) {
                aliasTable.remove(alias);
            }
        }
        return true;
    }
}
