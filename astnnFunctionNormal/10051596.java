class BackupThread extends Thread {
    private void broadcastRoutingInfo(Collection<Link> links) {
        try {
            String s = "";
            for (Link link : links) {
                s += link.getDestinationUuid() + ": " + link.getAlises() + ", ";
            }
            Set<String> uuids = new HashSet<String>();
            for (Map<String, Link> routingTable : routingTables.values()) {
                for (String uuid : routingTable.keySet()) {
                    if ((routingTable.get(uuid) != null && routingTable.get(uuid).getChannel() != null) && !routingTable.get(uuid).getChannel().isReceiveRoutingInfo()) {
                        continue;
                    }
                    uuids.add(uuid);
                }
            }
            for (String uuid : uuids) {
                if (UuidHelper.getSegmentFromEndpointNameOrEndpointUuid(uuid).equals(segment) && UuidHelper.isRouterUuid(uuid)) {
                    sendRouterInfo(uuid, constructRoutingInfo(uuid, links));
                    logger.trace("RouterInfo from: " + router.getCOOSInstanceName() + ", to:" + uuid + ":: " + s);
                }
            }
            sendRouterInfo(routerUuid, constructLocalRoutingInfo(links));
        } catch (Exception e) {
            logger.error("Exception ignored.", e);
        }
    }
}
