class BackupThread extends Thread {
    public List<NodeChannel> getNodeChannels(final String nodeId, boolean refreshExtractMillis) {
        boolean loaded = false;
        long channelCacheTimeoutInMs = parameterService.getLong(ParameterConstants.CACHE_TIMEOUT_CHANNEL_IN_MS);
        List<NodeChannel> nodeChannels = nodeChannelCache != null ? nodeChannelCache.get(nodeId) : null;
        if (System.currentTimeMillis() - nodeChannelCacheTime >= channelCacheTimeoutInMs || nodeChannels == null) {
            synchronized (this) {
                if (System.currentTimeMillis() - nodeChannelCacheTime >= channelCacheTimeoutInMs || nodeChannelCache == null || nodeChannelCache.get(nodeId) == null || nodeChannels == null) {
                    if (System.currentTimeMillis() - nodeChannelCacheTime >= channelCacheTimeoutInMs || nodeChannelCache == null) {
                        nodeChannelCache = new HashMap<String, List<NodeChannel>>();
                        nodeChannelCacheTime = System.currentTimeMillis();
                    }
                    if (nodeId != null) {
                        nodeChannels = sqlTemplate.query(getSql("selectNodeChannelsSql"), new ISqlRowMapper<NodeChannel>() {

                            public NodeChannel mapRow(Row row) {
                                NodeChannel nodeChannel = new NodeChannel();
                                nodeChannel.setChannelId(row.getString("channel_id"));
                                nodeChannel.setNodeId(nodeId);
                                nodeChannel.setIgnoreEnabled(row.getBoolean("ignore_enabled"));
                                nodeChannel.setSuspendEnabled(row.getBoolean("suspend_enabled"));
                                nodeChannel.setProcessingOrder(row.getInt("processing_order"));
                                nodeChannel.setMaxBatchSize(row.getInt("max_batch_size"));
                                nodeChannel.setEnabled(row.getBoolean("enabled"));
                                nodeChannel.setMaxBatchToSend(row.getInt("max_batch_to_send"));
                                nodeChannel.setMaxDataToRoute(row.getInt("max_data_to_route"));
                                nodeChannel.setUseOldDataToRoute(row.getBoolean("use_old_data_to_route"));
                                nodeChannel.setUseRowDataToRoute(row.getBoolean("use_row_data_to_route"));
                                nodeChannel.setUsePkDataToRoute(row.getBoolean("use_pk_data_to_route"));
                                nodeChannel.setContainsBigLobs(row.getBoolean("contains_big_lob"));
                                nodeChannel.setBatchAlgorithm(row.getString("batch_algorithm"));
                                nodeChannel.setLastExtractTime(row.getDateTime("last_extract_time"));
                                nodeChannel.setExtractPeriodMillis(row.getLong("extract_period_millis"));
                                nodeChannel.setDataLoaderType(row.getString("data_loader_type"));
                                return nodeChannel;
                            }

                            ;
                        }, nodeId);
                        nodeChannelCache.put(nodeId, nodeChannels);
                        loaded = true;
                    } else {
                        nodeChannels = new ArrayList<NodeChannel>(0);
                    }
                }
            }
        }
        if (!loaded && refreshExtractMillis) {
            final Map<String, NodeChannel> nodeChannelsMap = new HashMap<String, NodeChannel>();
            for (NodeChannel nc : nodeChannels) {
                nodeChannelsMap.put(nc.getChannelId(), nc);
            }
            sqlTemplate.query(getSql("selectNodeChannelControlLastExtractTimeSql"), new ISqlRowMapper<Object>() {

                public Object mapRow(Row row) {
                    String channelId = row.getString("channel_id");
                    Date extractTime = row.getDateTime("last_extract_time");
                    nodeChannelsMap.get(channelId).setLastExtractTime(extractTime);
                    return nodeChannelsMap;
                }

                ;
            }, nodeId);
        }
        return nodeChannels;
    }
}
