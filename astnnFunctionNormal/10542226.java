class BackupThread extends Thread {
    public StandardProject createSampleProjectDelegate(String projectName, boolean includeAjc) {
        createNoDepArchive();
        AntTarget target = new AntTarget("sample");
        ProjectLayout layout = delegate.getLayout();
        target.initLogging(layout.getLogFile(), Project.MSG_INFO);
        Java java = new Java();
        target.addTask(java);
        java.setTaskName("sample-java");
        java.setJar(getNoDepFile());
        java.createArg().setValue("-sample");
        java.createArg().setValue(projectName);
        java.setFork(true);
        java.setFailonerror(true);
        java.setDir(layout.getTargetDir());
        target.execute();
        File rootDir = new File(layout.getTargetDir(), projectName);
        StandardProject sampleProject = new StandardProject(rootDir, projectName, null);
        File protoLibDir = core.getCore().getLayout().getLibDir();
        File sampleLibDir = sampleProject.getLayout().getLibDir();
        if (includeAjc) {
            File aspectjtools = new File(protoLibDir, "aspectjtools.jar");
            FileUtils.copyFileToDirectory(aspectjtools, sampleLibDir);
            File aspectjweaver = new File(protoLibDir, "aspectjweaver.jar");
            FileUtils.copyFileToDirectory(aspectjweaver, sampleLibDir);
            File aspectjrt = new File(protoLibDir, "aspectjrt.jar");
            FileUtils.copyFileToDirectory(aspectjrt, sampleLibDir);
        }
        return sampleProject;
    }
}
