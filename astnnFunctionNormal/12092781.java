class BackupThread extends Thread {
    private void broadcastRoutingInfo(Map<String, Link> routingTable) {
        try {
            String s = "";
            for (Link link : routingTable.values()) {
                s += link.getDestinationUuid() + ": " + link.getAlises() + ", ";
            }
            Set<String> uuids = new HashSet<String>();
            for (Link link : routingTable.values()) {
                if ((link.getChannel() != null) && !link.getChannel().isReceiveRoutingInfo()) {
                    continue;
                }
                uuids.add(link.getDestinationUuid());
            }
            for (String uuid : uuids) {
                if (UuidHelper.getSegmentFromEndpointNameOrEndpointUuid(uuid).equals(segment) && UuidHelper.isRouterUuid(uuid)) {
                    sendRouterInfo(uuid, constructRoutingInfo(uuid, routingTable));
                    logger.trace("RouterInfo from: " + router.getCOOSInstanceName() + ", to:" + uuid + ":: " + s);
                }
            }
            sendRouterInfo(routerUuid, constructLocalRoutingInfo(links.values()));
        } catch (Exception e) {
            logger.error("Exception ignored.", e);
        }
    }
}
