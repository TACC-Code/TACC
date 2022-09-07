class BackupThread extends Thread {
    protected int selectDataAndRoute(ChannelRouterContext context) throws SQLException {
        IDataToRouteReader reader = startReading(context);
        Data data = null;
        int totalDataCount = 0;
        int totalDataEventCount = 0;
        int statsDataCount = 0;
        int statsDataEventCount = 0;
        final int maxNumberOfEventsBeforeFlush = parameterService.getInt(ParameterConstants.ROUTING_FLUSH_JDBC_BATCH_SIZE);
        try {
            do {
                data = reader.take();
                if (data != null) {
                    context.setLastDataIdProcessed(data.getDataId());
                    statsDataCount++;
                    totalDataCount++;
                    int dataEventsInserted = routeData(data, context);
                    statsDataEventCount += dataEventsInserted;
                    totalDataEventCount += dataEventsInserted;
                    long insertTs = System.currentTimeMillis();
                    try {
                        if (maxNumberOfEventsBeforeFlush <= context.getDataEventList().size() || context.isNeedsCommitted()) {
                            dataService.insertDataEvents(context.getSqlTransaction(), context.getDataEventList());
                            context.clearDataEventsList();
                        }
                        if (context.isNeedsCommitted()) {
                            completeBatchesAndCommit(context);
                        }
                    } finally {
                        context.incrementStat(System.currentTimeMillis() - insertTs, ChannelRouterContext.STAT_INSERT_DATA_EVENTS_MS);
                        if (statsDataCount > StatisticConstants.FLUSH_SIZE_ROUTER_DATA) {
                            statisticManager.incrementDataRouted(context.getChannel().getChannelId(), statsDataCount);
                            statsDataCount = 0;
                            statisticManager.incrementDataEventInserted(context.getChannel().getChannelId(), statsDataEventCount);
                            statsDataEventCount = 0;
                        }
                    }
                }
            } while (data != null);
        } finally {
            reader.setReading(false);
            if (statsDataCount > 0) {
                statisticManager.incrementDataRouted(context.getChannel().getChannelId(), statsDataCount);
            }
            if (statsDataEventCount > 0) {
                statisticManager.incrementDataEventInserted(context.getChannel().getChannelId(), statsDataEventCount);
            }
        }
        context.incrementStat(totalDataCount, ChannelRouterContext.STAT_DATA_ROUTED_COUNT);
        return totalDataEventCount;
    }
}
