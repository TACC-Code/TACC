class BackupThread extends Thread {
    public boolean isAlreadyInList(ScheduleItem schItem, int checkType) {
        if (checkType == 0) return false;
        DataStore store = DataStore.getInstance();
        ScheduleItem[] itemsArray = store.getScheduleArray();
        for (int x = 0; x < itemsArray.length; x++) {
            ScheduleItem item = itemsArray[x];
            if (schItem.equals(item)) return true;
            if (checkType == 2 || schItem.getChannel().equals(item.getChannel())) {
                if (schItem.getCreatedFrom() != null && item.getCreatedFrom() != null) {
                    if (schItem.getCreatedFrom().matches(item.getCreatedFrom())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
