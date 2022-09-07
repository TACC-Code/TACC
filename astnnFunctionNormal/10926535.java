class BackupThread extends Thread {
    public void execute() throws MojoExecutionException, MojoFailureException {
        this.locator.addSearchPath(FileResourceLoader.ID, this.project.getFile().getParentFile().getAbsolutePath());
        this.locator.addSearchPath("url", "");
        executeN2A();
        try {
            if (!FileUtils.fileExists("target/uibuilder")) FileUtils.mkdir("target/uibuilder");
            if (!FileUtils.fileExists("target/uibuilder/resources-common.xsl")) {
                File xslFile = this.locator.getResourceAsFile("xsl/resources-common.xsl");
                FileUtils.copyFile(xslFile, new File("target/uibuilder/resources-common.xsl"));
            }
            executeXslt("");
            executeXslt("_zh_CN");
            if (this.cleanUp) FileUtils.forceDelete("target/uibuilder");
        } catch (Exception e) {
            getLog().error(e);
        }
    }
}
