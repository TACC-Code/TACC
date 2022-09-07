class BackupThread extends Thread {
    protected int routeData(Data data, ChannelRouterContext context) throws SQLException {
        int numberOfDataEventsInserted = 0;
        context.recordTransactionBoundaryEncountered(data);
        List<TriggerRouter> triggerRouters = getTriggerRoutersForData(data);
        if (triggerRouters != null && triggerRouters.size() > 0) {
            for (TriggerRouter triggerRouter : triggerRouters) {
                Table table = symmetricDialect.getTable(triggerRouter.getTrigger(), true);
                DataMetaData dataMetaData = new DataMetaData(data, table, triggerRouter, context.getChannel());
                Collection<String> nodeIds = null;
                if (!context.getChannel().isIgnoreEnabled() && triggerRouter.isRouted(data.getDataEventType())) {
                    IDataRouter dataRouter = getDataRouter(triggerRouter);
                    context.addUsedDataRouter(dataRouter);
                    long ts = System.currentTimeMillis();
                    nodeIds = dataRouter.routeToNodes(context, dataMetaData, findAvailableNodes(triggerRouter, context), false);
                    context.incrementStat(System.currentTimeMillis() - ts, ChannelRouterContext.STAT_DATA_ROUTER_MS);
                    if (!triggerRouter.isPingBackEnabled() && data.getSourceNodeId() != null && nodeIds != null) {
                        nodeIds.remove(data.getSourceNodeId());
                    }
                }
                numberOfDataEventsInserted += insertDataEvents(context, dataMetaData, nodeIds, triggerRouter);
            }
        } else {
            log.warn("Could not find trigger for trigger id of {}.  Not processing data with the id of {}", data.getTriggerHistory().getTriggerId(), data.getDataId());
        }
        context.incrementStat(numberOfDataEventsInserted, ChannelRouterContext.STAT_DATA_EVENTS_INSERTED);
        return numberOfDataEventsInserted;
    }
}
