class BackupThread extends Thread {
    void calculateOptimalPaths(String qos) {
        logger.debug(this.router.getCOOSInstanceName() + ": Calculating optimal paths for: " + topologyMap.getRouterUuid() + " QoS: " + qos);
        Map<String, LinkCost> optimalPath = new HashMap<String, LinkCost>();
        Set<String> uuids = topologyMap.getNodeUuids();
        uuids.remove(topologyMap.getRouterUuid());
        Iterator<String> iter = uuids.iterator();
        while (iter.hasNext()) {
            String uuid = iter.next();
            optimalPath.put(uuid, new LinkCost(topologyMap.getLinkCost(uuid)));
        }
        while (!uuids.isEmpty()) {
            LinkCost minimalCost = null;
            iter = optimalPath.keySet().iterator();
            while (iter.hasNext()) {
                String uuid = iter.next();
                if (uuids.contains(uuid)) {
                    if ((minimalCost == null) || ((optimalPath.get(uuid)).getCost(qos) < minimalCost.getCost(qos))) {
                        minimalCost = optimalPath.get(uuid);
                    }
                }
            }
            String minimalCostUuid = minimalCost.getToUuid();
            uuids.remove(minimalCostUuid);
            iter = uuids.iterator();
            while (iter.hasNext()) {
                String nodeUuid = iter.next();
                if (topologyMap.isNeighbourNode(minimalCostUuid, nodeUuid)) {
                    int candidateCost = minimalCost.getCost(qos) + topologyMap.getLinkCost(minimalCostUuid, nodeUuid).getCost(qos);
                    int currentCost;
                    if (optimalPath.get(nodeUuid) != null) {
                        currentCost = (optimalPath.get(nodeUuid)).getCost(qos);
                    } else {
                        currentCost = topologyMap.getLinkCost(nodeUuid).getCost(qos);
                    }
                    if (candidateCost < currentCost) {
                        LinkCost linkCost = optimalPath.get(nodeUuid);
                        linkCost.setCost(qos, candidateCost);
                        linkCost.setNextLinkCost(optimalPath.get(minimalCostUuid));
                    }
                }
            }
        }
        Iterator<LinkCost> valIter = optimalPath.values().iterator();
        while (valIter.hasNext()) {
            LinkCost linkCost = valIter.next();
            String toUuid = linkCost.getToUuid();
            while (linkCost.getNextLink() != null) {
                linkCost = linkCost.getNextLink();
            }
            Link l = links.get(linkCost.getLinkId());
            if (linkCost.getCost(qos) < LinkCost.MAX_VALUE) {
                if (l != null) {
                    routingTables.get(qos).put(toUuid, links.get(linkCost.getLinkId()));
                    for (String alias : topologyMap.getAliases(toUuid)) {
                        router.putAlias(alias, toUuid);
                    }
                }
            } else {
                routingTables.get(qos).remove(toUuid);
                for (String alias : topologyMap.getAliases(toUuid)) {
                    router.removeAlias(alias);
                }
                if ((l != null) && (l.getChannel() != null) && !l.getChannel().isDefaultGw()) {
                    links.remove(linkCost.getLinkId());
                    if (loggingEnabled) {
                        logger.debug(routerUuid + " removing from routerTable Link to: " + linkCost.getToUuid());
                    }
                }
            }
        }
        if (loggingEnabled) {
            printRoutingTable(routerUuid, qos, routingTables.get(qos), logger);
            printAliasTable(routerUuid, aliasTable, logger);
            printOptimalPath(routerUuid, qos, optimalPath, logger);
        }
    }
}
