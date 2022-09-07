class BackupThread extends Thread {
    public void execute() throws MojoExecutionException, MojoFailureException {
        super.execute();
        List unpackTypesList = new ArrayList();
        if (unpackTypes != null) {
            unpackTypesList = Arrays.asList(unpackTypes.split(","));
            final Iterator it = unpackTypesList.iterator();
            while (it.hasNext()) {
                String type = (String) it.next();
                if (!EarModuleFactory.standardArtifactTypes.contains(type)) {
                    throw new MojoExecutionException("Invalid type[" + type + "] supported types are " + EarModuleFactory.standardArtifactTypes);
                }
            }
            getLog().debug("Initialized unpack types " + unpackTypesList);
        }
        try {
            for (Iterator iter = getModules().iterator(); iter.hasNext(); ) {
                EarModule module = (EarModule) iter.next();
                if (module instanceof JavaModule) {
                    getLog().warn("JavaModule is deprecated (" + module + "), please use JarModule instead.");
                }
                if (module instanceof Ejb3Module) {
                    getLog().warn("Ejb3Module is deprecated (" + module + "), please use EjbModule instead.");
                }
                final File sourceFile = module.getArtifact().getFile();
                final File destinationFile = buildDestinationFile(getWorkDirectory(), module.getUri());
                if (!sourceFile.isFile()) {
                    throw new MojoExecutionException("Cannot copy a directory: " + sourceFile.getAbsolutePath() + "; Did you package/install " + module.getArtifact() + "?");
                }
                if (destinationFile.getCanonicalPath().equals(sourceFile.getCanonicalPath())) {
                    getLog().info("Skipping artifact[" + module + "], as it already exists at[" + module.getUri() + "]");
                    continue;
                }
                if ((unpackTypesList.contains(module.getType()) && (module.shouldUnpack() == null || module.shouldUnpack().booleanValue())) || (module.shouldUnpack() != null && module.shouldUnpack().booleanValue())) {
                    getLog().info("Copying artifact[" + module + "] to[" + module.getUri() + "] (unpacked)");
                    destinationFile.mkdirs();
                    unpack(sourceFile, destinationFile);
                } else {
                    if (sourceFile.lastModified() > destinationFile.lastModified()) {
                        getLog().info("Copying artifact[" + module + "] to[" + module.getUri() + "]");
                        FileUtils.copyFile(sourceFile, destinationFile);
                    } else {
                        getLog().debug("Skipping artifact[" + module + "], as it is already up to date at[" + module.getUri() + "]");
                    }
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error copying EAR modules", e);
        } catch (ArchiverException e) {
            throw new MojoExecutionException("Error unpacking EAR modules", e);
        } catch (NoSuchArchiverException e) {
            throw new MojoExecutionException("No Archiver found for EAR modules", e);
        }
        try {
            File earSourceDir = earSourceDirectory;
            if (earSourceDir.exists()) {
                getLog().info("Copy ear sources to " + getWorkDirectory().getAbsolutePath());
                String[] fileNames = getEarFiles(earSourceDir);
                for (int i = 0; i < fileNames.length; i++) {
                    copyFile(new File(earSourceDir, fileNames[i]), new File(getWorkDirectory(), fileNames[i]));
                }
            }
            if (applicationXml != null && !"".equals(applicationXml)) {
                getLog().info("Including custom application.xml[" + applicationXml + "]");
                File metaInfDir = new File(getWorkDirectory(), META_INF);
                copyFile(new File(applicationXml), new File(metaInfDir, "/application.xml"));
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error copying EAR sources", e);
        } catch (MavenFilteringException e) {
            throw new MojoExecutionException("Error filetering EAR sources", e);
        }
        try {
            if (resourcesDir != null && resourcesDir.exists()) {
                getLog().warn("resourcesDir is deprecated. Please use the earSourceDirectory property instead.");
                getLog().info("Copy ear resources to " + getWorkDirectory().getAbsolutePath());
                String[] fileNames = getEarFiles(resourcesDir);
                for (int i = 0; i < fileNames.length; i++) {
                    FileUtils.copyFile(new File(resourcesDir, fileNames[i]), new File(getWorkDirectory(), fileNames[i]));
                }
            }
        } catch (IOException e) {
            throw new MojoExecutionException("Error copying EAR resources", e);
        }
        File ddFile = new File(getWorkDirectory(), APPLICATION_XML_URI);
        if (!ddFile.exists() && !version.equals(VERSION_5)) {
            throw new MojoExecutionException("Deployment descriptor: " + ddFile.getAbsolutePath() + " does not exist.");
        }
        performPackaging();
    }
}
