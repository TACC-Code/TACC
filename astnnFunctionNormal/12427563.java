class BackupThread extends Thread {
    private int numOverlaps(ScheduleItem schItem, StringBuffer buff) {
        DataStore store = DataStore.getInstance();
        Vector<ScheduleItem> overlapping = new Vector<ScheduleItem>();
        SimpleDateFormat df = new SimpleDateFormat("EE d HH:mm");
        ScheduleItem[] itemsArray = store.getScheduleArray();
        for (int x = 0; x < itemsArray.length; x++) {
            ScheduleItem item = itemsArray[x];
            if (item.isOverlapping(schItem)) {
                overlapping.add(item);
                buff.append(item.getName() + " (" + df.format(item.getStart()));
                buff.append(" " + item.getDuration() + " ");
                buff.append(item.getChannel() + ")<br>\n");
            }
        }
        if (overlapping.size() == 0) return 1;
        int overlapCount = getOverlapCount(schItem, overlapping);
        return overlapCount;
    }
}
