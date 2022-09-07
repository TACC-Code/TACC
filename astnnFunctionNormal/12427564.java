class BackupThread extends Thread {
    private int getOverlapCount(ScheduleItem item, Vector<ScheduleItem> overlapping) {
        Date start = item.getStart();
        Date end = item.getStop();
        DataStore store = DataStore.getInstance();
        HashMap<String, Channel> channels = store.getChannels();
        Channel schChannel = channels.get(item.getChannel());
        String muxString = schChannel.getFrequency() + "-" + schChannel.getBandWidth();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(start);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(end);
        int maxOverlap = 0;
        while (startCal.before(endCal)) {
            Calendar span = Calendar.getInstance();
            span.setTime(startCal.getTime());
            span.add(Calendar.MINUTE, 1);
            HashMap<String, Integer> muxOverlapCount = new HashMap<String, Integer>();
            muxOverlapCount.put(muxString, new Integer(1));
            getOverlapsForMin(startCal.getTime(), span.getTime(), muxOverlapCount, overlapping, channels);
            Integer[] count = (Integer[]) muxOverlapCount.values().toArray(new Integer[0]);
            if (maxOverlap < count.length) maxOverlap = count.length;
            startCal.add(Calendar.MINUTE, 1);
        }
        return maxOverlap;
    }
}
