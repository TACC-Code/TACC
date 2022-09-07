class BackupThread extends Thread {
    @Test
    public void testSingleModule() throws Exception {
        Helper.doCmd(wc, "svn", "update", "underworld/trunk");
        final File myModule = new File(wc, "underworld/trunk/single");
        myModule.mkdirs();
        FileUtils.copyFile(new File(resources, "data/simple.pom.xml"), new File(myModule, "pom.xml"));
        final File changesXml = new File(myModule, "changes.xml");
        FileUtils.copyFile(new File(resources, "data/simple.changes.xml"), changesXml);
        Helper.doCmd(wc, "svn", "add", myModule.getAbsolutePath());
        Helper.doCmd(wc, "svn", "ci", "-m", "adding module", myModule.getAbsolutePath());
        JReleasator.main("--tmpbase", tmpbase, "--settings", settingsFile.getAbsolutePath(), "--author", "releasator@buildbox.sf.net", "full", repoUrl + "/underworld/trunk/single", "1.0.0-alpha-1");
        Helper.doCmd(wc, "svn", "update");
        final ChangesDocumentBean changes = ChangesDocumentBean.Factory.parse(changesXml);
        final String tag = changes.getChanges().getReleaseArray(0).getVcs().getTag();
        Assert.assertEquals("/underworld/tags/net.sf.buildbox.releasator.integration-test-test-simple-1.0.0-alpha-1", tag);
    }
}
