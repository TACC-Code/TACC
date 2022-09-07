class BackupThread extends Thread {
    public void test_All() throws IOException, Exception {
        setUp();
        FilteredTVData filtData = new FilteredTVData(data, ext, storage, application);
        new XMLTVExport().exportToWriter(writer, filtData);
        String xml = writer.toString();
        FreeGuideTest.my_assert(contains_channel(filtData.getChannels(), chan1));
        FreeGuideTest.my_assert(contains_channel(filtData.getChannels(), chan2));
        FreeGuideTest.my_assert(xml.contains(prog11.getTitle()));
        FreeGuideTest.my_assert(xml.contains(prog12.getTitle()));
        FreeGuideTest.my_assert(xml.contains(prog13.getTitle()));
        FreeGuideTest.my_assert(xml.contains(prog21.getTitle()));
        FreeGuideTest.my_assert(xml.contains(prog22.getTitle()));
    }
}
