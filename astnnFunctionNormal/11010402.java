class BackupThread extends Thread {
    public Map<String, Object> getParams(Channel channel) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("CHANNEL_ID", channel.getChannelId());
        params.put("BATCH_ALGORITHM", channel.getBatchAlgorithm());
        params.put("CONTAINS_BIG_LOB", channel.isContainsBigLob());
        params.put("ENABLED", channel.isEnabled());
        params.put("EXTRACT_PERIOD_MILLIS", channel.getExtractPeriodMillis());
        params.put("MAX_BATCH_SIZE", channel.getMaxBatchSize());
        params.put("MAX_BATCH_TO_SEND", channel.getMaxBatchToSend());
        params.put("MAX_DATA_TO_ROUTE", channel.getMaxDataToRoute());
        params.put("PROCESSING_ORDER", channel.getProcessingOrder());
        params.put("USE_OLD_DATA_TO_ROUTE", channel.isUseOldDataToRoute());
        params.put("USE_PK_DATA_TO_ROUTE", channel.isUsePkDataToRoute());
        params.put("USE_ROW_DATA_TO_ROUTE", channel.isUseRowDataToRoute());
        return params;
    }
}
