class BackupThread extends Thread {
    @Test
    public void testMultiModuleWithOneChild() throws Exception {
        Helper.doCmd(wc, "svn", "update", "trunk");
        final File simple = new File(wc, "trunk/multi1/simple");
        simple.mkdirs();
        final File multi = simple.getParentFile();
        FileUtils.copyFile(new File(resources, "data/multi1.pom.xml"), new File(multi, "pom.xml"));
        FileUtils.copyFile(new File(resources, "data/multi.changes.xml"), new File(multi, "changes.xml"));
        FileUtils.copyFile(new File(resources, "data/simple.pom.xml"), new File(simple, "pom.xml"));
        Helper.doCmd(wc, "svn", "add", multi.getAbsolutePath());
        Helper.doCmd(wc, "svn", "ci", "-m", "adding modules", multi.getAbsolutePath());
        JReleasator.main("--tmpbase", tmpbase, "--settings", settingsFile.getAbsolutePath(), "--author", "releasator@buildbox.sf.net", "full", "scm:svn:" + repoUrl + "/trunk/multi1", "1.0.0-alpha-2", "container with one child");
    }
}
