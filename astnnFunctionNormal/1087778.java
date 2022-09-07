class BackupThread extends Thread {
    protected int insertDataEvents(ChannelRouterContext context, DataMetaData dataMetaData, Collection<String> nodeIds, TriggerRouter triggerRouter) {
        int numberOfDataEventsInserted = 0;
        if (nodeIds == null || nodeIds.size() == 0) {
            nodeIds = new HashSet<String>(1);
            nodeIds.add(Constants.UNROUTED_NODE_ID);
        }
        long ts = System.currentTimeMillis();
        for (String nodeId : nodeIds) {
            Map<String, OutgoingBatch> batches = context.getBatchesByNodes();
            OutgoingBatch batch = batches.get(nodeId);
            if (batch == null) {
                batch = new OutgoingBatch(nodeId, dataMetaData.getNodeChannel().getChannelId(), Status.RT);
                outgoingBatchService.insertOutgoingBatch(batch);
                context.getBatchesByNodes().put(nodeId, batch);
            }
            batch.incrementEventCount(dataMetaData.getData().getDataEventType());
            batch.incrementDataEventCount();
            numberOfDataEventsInserted++;
            context.addDataEvent(dataMetaData.getData().getDataId(), batch.getBatchId(), triggerRouter.getRouter().getRouterId());
            if (batchAlgorithms.get(context.getChannel().getBatchAlgorithm()).isBatchComplete(batch, dataMetaData, context)) {
                context.setNeedsCommitted(true);
            }
        }
        context.incrementStat(System.currentTimeMillis() - ts, ChannelRouterContext.STAT_INSERT_DATA_EVENTS_MS);
        return numberOfDataEventsInserted;
    }
}
