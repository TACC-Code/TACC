class BackupThread extends Thread {
    public RocketTable(int tableId) {
        super("Table" + tableId);
        Channel tableChannel = AppContext.getChannelManager().createChannel(getName() + "-channel", this, Delivery.RELIABLE);
        tableChannelRef = AppContext.getDataManager().createReference(tableChannel);
        gameEngine = new RocketGameEngine(this);
        setCurrentStatus(TableStatus.EMPTY);
    }
}
