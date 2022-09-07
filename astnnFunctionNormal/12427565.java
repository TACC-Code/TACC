class BackupThread extends Thread {
    private void getOverlapsForMin(Date start, Date end, HashMap<String, Integer> muxOverlapCount, Vector<ScheduleItem> overlapping, HashMap<String, Channel> channels) {
        for (int x = 0; x < overlapping.size(); x++) {
            ScheduleItem item = overlapping.get(x);
            Channel schChannel = channels.get(item.getChannel());
            String muxString = schChannel.getFrequency() + "-" + schChannel.getBandWidth();
            boolean overlap = false;
            if (item.getStart().getTime() >= start.getTime() && item.getStart().getTime() < end.getTime()) overlap = true;
            if (item.getStop().getTime() > start.getTime() && item.getStop().getTime() <= end.getTime()) overlap = true;
            if (item.getStart().getTime() <= start.getTime() && item.getStop().getTime() >= end.getTime()) overlap = true;
            if (overlap) {
                Integer muxCount = muxOverlapCount.get(muxString);
                if (muxCount == null) {
                    muxOverlapCount.put(muxString, new Integer(1));
                } else {
                    muxOverlapCount.put(muxString, new Integer(muxCount.intValue() + 1));
                }
            }
        }
    }
}
