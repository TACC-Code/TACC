class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public static List<IContribution> getContributionList(String channelName) {
        return (List<IContribution>) getChannelCache().get(channelName).getObjectValue();
    }
}
