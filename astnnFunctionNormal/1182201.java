class BackupThread extends Thread {
    protected void initialize() throws MojoExecutionException {
        if (initialized) {
            return;
        }
        getLog().debug("Using hibernate cfg file: " + hibernateConfig);
        if (!hibernateConfig.exists()) {
            throw new MojoExecutionException("No hibernate config file found: " + hibernateConfig);
        }
        if (!targetPath.exists()) {
            targetPath.mkdirs();
        }
        File speciesFile = getSpeciesFile();
        File publicationsFile = getPublicationsFile();
        if (speciesFile.exists() && !overwrite) {
            throw new MojoExecutionException("Target species file already exist and overwrite is set to false: " + speciesFile);
        }
        if (publicationsFile.exists() && !overwrite) {
            throw new MojoExecutionException("Target publications file already exist and overwrite is set to false: " + speciesFile);
        }
        IntactSession session = new StandaloneSession();
        CustomCoreDataConfig testConfig = new CustomCoreDataConfig("PsiXmlGeneratorMojoTest", hibernateConfig, session);
        testConfig.initialize();
        IntactContext.initContext(testConfig, session);
        experimentListGenerator = new ExperimentListGenerator(searchPattern);
        experimentListGenerator.setOnlyWithPmid(onlyWithPmid);
        initialized = true;
    }
}
