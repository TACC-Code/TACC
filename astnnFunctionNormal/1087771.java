class BackupThread extends Thread {
    protected int routeDataForChannel(final NodeChannel nodeChannel, final Node sourceNode) {
        ChannelRouterContext context = null;
        long ts = System.currentTimeMillis();
        int dataCount = -1;
        try {
            context = new ChannelRouterContext(sourceNode.getNodeId(), nodeChannel, symmetricDialect.getPlatform().getSqlTemplate().startSqlTransaction());
            dataCount = selectDataAndRoute(context);
            return dataCount;
        } catch (Exception ex) {
            log.error(String.format("Failed to route and batch data on '%s' channel", nodeChannel.getChannelId()), ex);
            if (context != null) {
                context.rollback();
            }
            return 0;
        } finally {
            try {
                if (dataCount > 0) {
                    long insertTs = System.currentTimeMillis();
                    dataService.insertDataEvents(context.getSqlTransaction(), context.getDataEventList());
                    context.clearDataEventsList();
                    completeBatchesAndCommit(context);
                    context.incrementStat(System.currentTimeMillis() - insertTs, ChannelRouterContext.STAT_INSERT_DATA_EVENTS_MS);
                    if (context.getLastDataIdProcessed() > 0) {
                        String channelId = nodeChannel.getChannelId();
                        long queryTs = System.currentTimeMillis();
                        long dataLeftToRoute = sqlTemplate.queryForInt(getSql("selectUnroutedCountForChannelSql"), channelId, context.getLastDataIdProcessed());
                        queryTs = System.currentTimeMillis() - queryTs;
                        if (queryTs > Constants.LONG_OPERATION_THRESHOLD) {
                            log.warn("Unrouted query for channel {} took {} ms", channelId, queryTs);
                        }
                        statisticManager.setDataUnRouted(channelId, dataLeftToRoute);
                    }
                }
            } catch (Exception e) {
                if (context != null) {
                    context.rollback();
                }
                log.error(e.getMessage(), e);
            } finally {
                long totalTime = System.currentTimeMillis() - ts;
                context.incrementStat(totalTime, ChannelRouterContext.STAT_ROUTE_TOTAL_TIME);
                context.logStats(log, totalTime);
                context.cleanup();
            }
        }
    }
}
