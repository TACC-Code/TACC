class BackupThread extends Thread {
    public void test_SelectedTodayChannelSet() throws IOException, Exception {
        setUp();
        ext.getCbSelected().setSelected(true);
        FakeReminder reminder = new FakeReminder();
        reminder.selectedProgs.add(prog11);
        reminder.selectedProgs.add(prog22);
        application.reminders.add(reminder);
        ext.getCbToday().setSelected(true);
        cal.set(2009, 07, 30, 6, 0);
        viewer.info.minDate = cal.getTimeInMillis();
        cal.set(2009, 07, 31, 6, 0);
        viewer.info.maxDate = cal.getTimeInMillis();
        ext.getCbChannelsList().setSelected(true);
        ext.getSaveInfo().channelsList.add(new TVChannelsSet.Channel(chan2.getID(), chan2.getDisplayName()));
        FilteredTVData filtData = new FilteredTVData(data, ext, storage, application);
        new XMLTVExport().exportToWriter(writer, filtData);
        String xml = writer.toString();
        FreeGuideTest.my_assert(!contains_channel(filtData.getChannels(), chan1));
        FreeGuideTest.my_assert(contains_channel(filtData.getChannels(), chan2));
        FreeGuideTest.my_assert(!xml.contains(prog11.getTitle()));
        FreeGuideTest.my_assert(!xml.contains(prog12.getTitle()));
        FreeGuideTest.my_assert(!xml.contains(prog13.getTitle()));
        FreeGuideTest.my_assert(!xml.contains(prog21.getTitle()));
        FreeGuideTest.my_assert(xml.contains(prog22.getTitle()));
    }
}
