class BackupThread extends Thread {
    @Test
    public void testFile() throws IOException {
        TestFileUtils.verifyDirectory(base, "out");
        String name = filename.substring(0, filename.length() - ".xml".length());
        File cfg = TestFileUtils.getTransferredTestInputFile(testdataDirectory, "in/rescan", filename);
        String in = TestFileUtils.getTestInputFile(testdataDirectory, "in/rescan", name + ".ltm");
        File cxtm = TestFileUtils.getTestOutputFile(testdataDirectory, "out", "rescan-" + name + ".cxtm");
        String baseline = TestFileUtils.getTestInputFile(testdataDirectory, "baseline", "rescan-" + name + ".cxtm");
        TopicMapIF topicmap = ImportExportUtils.getReader(in).read();
        LocatorIF baseloc = topicmap.getStore().getBaseAddress();
        RelationMapping mapping = RelationMapping.read(cfg);
        File target = TestFileUtils.getTestOutputFile(testdataDirectory, "in", "rescan", name + ".csv");
        File before = TestFileUtils.getTestOutputFile(testdataDirectory, "in", "rescan", name + "-before.csv");
        File after = TestFileUtils.getTestOutputFile(testdataDirectory, "in", "rescan", name + "-after.csv");
        FileUtils.copyFile(before, target);
        Processor.addRelations(mapping, null, topicmap, baseloc);
        FileUtils.copyFile(after, target);
        Processor.synchronizeRelations(mapping, null, topicmap, baseloc);
        FileUtils.deleteFile(target);
        if (DEBUG_LTM) {
            File ltm = TestFileUtils.getTestOutputFile(testdataDirectory, "out", "rescan-" + name + ".ltm");
            (new LTMTopicMapWriter(new FileOutputStream(ltm))).write(topicmap);
        }
        FileOutputStream out = new FileOutputStream(cxtm);
        (new CanonicalXTMWriter(out)).write(topicmap);
        out.close();
        Assert.assertTrue("The canonicalized conversion from " + filename + " does not match the baseline: " + cxtm + " " + baseline, FileUtils.compareFileToResource(cxtm, baseline));
    }
}
