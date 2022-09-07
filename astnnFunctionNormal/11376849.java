class BackupThread extends Thread {
    public Map<String, Channel> getChannels(boolean refreshCache) {
        long channelCacheTimeoutInMs = parameterService.getLong(ParameterConstants.CACHE_TIMEOUT_CHANNEL_IN_MS, 60000);
        Map<String, Channel> channels = channelsCache;
        if (System.currentTimeMillis() - channelCacheTime >= channelCacheTimeoutInMs || channels == null || refreshCache) {
            synchronized (this) {
                channels = channelsCache;
                if (System.currentTimeMillis() - channelCacheTime >= channelCacheTimeoutInMs || channels == null || refreshCache) {
                    channels = new HashMap<String, Channel>();
                    List<Channel> list = sqlTemplate.query(getSql("selectChannelsSql"), new ISqlRowMapper<Channel>() {

                        public Channel mapRow(Row row) {
                            Channel channel = new Channel();
                            channel.setChannelId(row.getString("channel_id"));
                            channel.setProcessingOrder(row.getInt("processing_order"));
                            channel.setMaxBatchSize(row.getInt("max_batch_size"));
                            channel.setEnabled(row.getBoolean("enabled"));
                            channel.setMaxBatchToSend(row.getInt("max_batch_to_send"));
                            channel.setMaxDataToRoute(row.getInt("max_data_to_route"));
                            channel.setUseOldDataToRoute(row.getBoolean("use_old_data_to_route"));
                            channel.setUseRowDataToRoute(row.getBoolean("use_row_data_to_route"));
                            channel.setUsePkDataToRoute(row.getBoolean("use_pk_data_to_route"));
                            channel.setContainsBigLob(row.getBoolean("contains_big_lob"));
                            channel.setBatchAlgorithm(row.getString("batch_algorithm"));
                            channel.setExtractPeriodMillis(row.getLong("extract_period_millis"));
                            channel.setDataLoaderType(row.getString("data_loader_type"));
                            return channel;
                        }
                    });
                    for (Channel channel : list) {
                        channels.put(channel.getChannelId(), channel);
                    }
                    channelsCache = channels;
                    channelCacheTime = System.currentTimeMillis();
                }
            }
        }
        return channels;
    }
}
