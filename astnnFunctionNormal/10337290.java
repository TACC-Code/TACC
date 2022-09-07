class BackupThread extends Thread {
    @SuppressWarnings("unchecked")
    public L2ItemInstance createItem(String process, int itemId, int count, L2PcInstance actor, L2Object reference) {
        L2ItemInstance item = new L2ItemInstance(IdFactory.getInstance().getNextId(), itemId);
        if (process.equalsIgnoreCase("loot") && !Config.AUTO_LOOT) {
            ScheduledFuture itemLootShedule;
            long delay = 0;
            if (reference != null && reference instanceof L2GrandBossInstance || reference instanceof L2RaidBossInstance) {
                if (((L2Attackable) reference).getFirstCommandChannelAttacked() != null && ((L2Attackable) reference).getFirstCommandChannelAttacked().meetRaidWarCondition(reference)) {
                    item.setOwnerId(((L2Attackable) reference).getFirstCommandChannelAttacked().getChannelLeader().getObjectId());
                    delay = 300000;
                } else {
                    delay = 15000;
                    item.setOwnerId(actor.getObjectId());
                }
            } else {
                item.setOwnerId(actor.getObjectId());
                delay = 15000;
            }
            itemLootShedule = ThreadPoolManager.getInstance().scheduleGeneral(new resetOwner(item), delay);
            item.setItemLootShedule(itemLootShedule);
        }
        if (Config.DEBUG) _log.fine("ItemTable: Item created  oid:" + item.getObjectId() + " itemid:" + itemId);
        L2World.getInstance().storeObject(item);
        if (item.isStackable() && count > 1) item.setCount(count);
        if (Config.LOG_ITEMS) {
            LogRecord record = new LogRecord(Level.INFO, "CREATE:" + process);
            record.setLoggerName("item");
            record.setParameters(new Object[] { item, actor, reference });
            _logItems.log(record);
        }
        return item;
    }
}
