class BackupThread extends Thread {
    public DataQuery copyTreeBetweenWindows(DN sourceNodeDN, DN newNodeDN, DataBrokerUnthreadedInterface sourceData, boolean overwriteExistingData) {
        return push(new DataQuery(DataQuery.XWINCOPY, sourceNodeDN, newNodeDN, sourceData, overwriteExistingData));
    }
}
