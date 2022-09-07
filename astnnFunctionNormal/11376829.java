class BackupThread extends Thread {
    public void saveChannel(Channel channel, boolean reloadChannels) {
        if (0 == sqlTemplate.update(getSql("updateChannelSql"), new Object[] { channel.getProcessingOrder(), channel.getMaxBatchSize(), channel.getMaxBatchToSend(), channel.getMaxDataToRoute(), channel.isUseOldDataToRoute() ? 1 : 0, channel.isUseRowDataToRoute() ? 1 : 0, channel.isUsePkDataToRoute() ? 1 : 0, channel.isContainsBigLob() ? 1 : 0, channel.isEnabled() ? 1 : 0, channel.getBatchAlgorithm(), channel.getExtractPeriodMillis(), channel.getDataLoaderType(), channel.getChannelId() })) {
            sqlTemplate.update(getSql("insertChannelSql"), new Object[] { channel.getChannelId(), channel.getProcessingOrder(), channel.getMaxBatchSize(), channel.getMaxBatchToSend(), channel.getMaxDataToRoute(), channel.isUseOldDataToRoute() ? 1 : 0, channel.isUseRowDataToRoute() ? 1 : 0, channel.isUsePkDataToRoute() ? 1 : 0, channel.isContainsBigLob() ? 1 : 0, channel.isEnabled() ? 1 : 0, channel.getBatchAlgorithm(), channel.getExtractPeriodMillis(), channel.getDataLoaderType() });
        }
        if (reloadChannels) {
            reloadChannels();
        }
    }
}
