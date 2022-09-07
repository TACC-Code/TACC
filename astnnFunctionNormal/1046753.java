class BackupThread extends Thread {
    @Test
    public void testMultiModuleWithTwoChildren() throws Exception {
        Helper.doCmd(wc, "svn", "update", "trunk");
        final File first = new File(wc, "trunk/multi2/first");
        final File second = new File(wc, "trunk/multi2/second");
        first.mkdirs();
        final File multi = first.getParentFile();
        FileUtils.copyFile(new File(resources, "data/multi2.pom.xml"), new File(multi, "pom.xml"));
        FileUtils.copyFile(new File(resources, "data/multi.changes.xml"), new File(multi, "changes.xml"));
        FileUtils.copyFile(new File(resources, "data/simple.pom.xml"), new File(first, "pom.xml"));
        FileUtils.copyFile(new File(resources, "data/second.pom.xml"), new File(second, "pom.xml"));
        Helper.doCmd(wc, "svn", "add", multi.getAbsolutePath());
        Helper.doCmd(wc, "svn", "ci", "-m", "adding modules", multi.getAbsolutePath());
        JReleasator.main("--tmpbase", tmpbase, "--settings", settingsFile.getAbsolutePath(), "--author", "releasator@buildbox.sf.net", "full", "scm:svn:" + repoUrl + "/trunk/multi2", "1.0.0-alpha-3", "container with two children");
    }
}
