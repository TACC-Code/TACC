class BackupThread extends Thread {
    public void execute() throws MojoExecutionException {
        if (!isModuloPrincipal()) return;
        if (ci == null || ci.getCidependencies() == null) return;
        File baseArchives = getArtifactDir();
        for (String warProject : ci.getCidependencies()) {
            File baseArchivesProject = new File(baseArchives, warProject);
            File lastBuildDir = getUltimoBuild(baseArchivesProject);
            File warProjectFile = new File(lastBuildDir, warProject + ".war");
            File contextTest = new File(lastBuildDir, "context.xml");
            try {
                getLog().info("Fazendo deploy do projeto depend�ncia: " + warProject);
                warProjectFile = warProjectFile.getCanonicalFile();
                FileUtils.copyFile(warProjectFile, new File(getServer().getWebappsDir(), warProjectFile.getName()));
                getLog().info("Copiado arquivo: " + warProjectFile.getAbsolutePath());
                contextTest = contextTest.getAbsoluteFile();
                FileUtils.copyFile(contextTest, new File(getServer().getContextDir(), warProject + ".xml"));
                getLog().info("Copiado arquivo: " + contextTest.getAbsolutePath());
            } catch (Exception e) {
                throw new MojoExecutionException("N�o foi poss�vel fazer o deploy do projeto dependencia: " + warProjectFile.getAbsolutePath());
            }
        }
    }
}
