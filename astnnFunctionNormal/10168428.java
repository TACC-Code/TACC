class BackupThread extends Thread {
    private void fillLists(TVChannelsSet allChannelsSet) {
        cmbTitle.addItem(Application.getInstance().getLocalizedMessage("exactly"));
        cmbTitle.addItem(Application.getInstance().getLocalizedMessage("contains"));
        cmbTitle.addItem(Application.getInstance().getLocalizedMessage("regular_expression"));
        cmbChannel.addItem(StringHelper.EMPTY_STRING);
        Iterator it = allChannelsSet.getSortedChannels().iterator();
        while (it.hasNext()) {
            TVChannelsSet.Channel ch = (TVChannelsSet.Channel) it.next();
            cmbChannel.addItem(ch);
            channels.put(ch.getChannelID(), ch);
        }
        Calendar cal = GregorianCalendar.getInstance();
        cmbDayOfWeek.addItem(StringHelper.EMPTY_STRING);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cmbDayOfWeek.addItem(dayOfWeekFormat.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cmbDayOfWeek.addItem(dayOfWeekFormat.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
        cmbDayOfWeek.addItem(dayOfWeekFormat.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
        cmbDayOfWeek.addItem(dayOfWeekFormat.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        cmbDayOfWeek.addItem(dayOfWeekFormat.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        cmbDayOfWeek.addItem(dayOfWeekFormat.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        cmbDayOfWeek.addItem(dayOfWeekFormat.format(cal.getTime()));
    }
}
