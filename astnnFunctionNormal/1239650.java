class BackupThread extends Thread {
    public void execute() throws MojoExecutionException, MojoFailureException {
        final Map<String, String> artifactVersions = new HashMap<String, String>();
        for (Object o : project.getArtifacts()) {
            final Artifact a = (Artifact) o;
            if (!Artifact.SCOPE_RUNTIME.equals(a.getScope())) continue;
            final String ref = a.getGroupId() + ":" + a.getArtifactId();
            artifactVersions.put(ref, a.getVersion());
        }
        for (Artifact a : pluginArtifacts) {
            final String ref = a.getGroupId() + ":" + a.getArtifactId();
            artifactVersions.put(ref, a.getVersion());
        }
        if (launchers != null) {
            try {
                outputFile.getParentFile().mkdirs();
                final InstallerDocument doc = InstallerDocument.Factory.newInstance();
                final InstallerBean installerBean = doc.addNewInstaller();
                final String tk = project.getGroupId() + "_" + project.getArtifactId();
                final String toolkit = tk.toUpperCase().replaceAll("[-\\.]", "_");
                installerBean.setToolkit(toolkit);
                installerBean.setBundledBy(MojoUtils.getInstallerId());
                for (LauncherDef launcherDef : launchers) {
                    logger.log(CAT.pathDef(launcherDef.getId(), launcherDef.isTransitive()));
                    final Set<Artifact> primaryArtifacts = new LinkedHashSet<Artifact>();
                    final Set<Artifact> resultArtifacts = new LinkedHashSet<Artifact>();
                    for (String ga : launcherDef.getClasspath()) {
                        logger.log(CAT.locating(ga));
                        final Artifact a = locateArtifact(artifactVersions, ga);
                        if (launcherDef.isTransitive()) {
                            primaryArtifacts.add(a);
                        } else {
                            if (!isMyself(a)) {
                                resolve(a, false);
                            }
                            resultArtifacts.add(a);
                        }
                    }
                    final Set<Artifact> primaryArtifactsReduced = new LinkedHashSet<Artifact>();
                    boolean usingSelf = false;
                    for (Artifact primaryArtifact : primaryArtifacts) {
                        if (isMyself(primaryArtifact)) {
                            usingSelf = true;
                            continue;
                        }
                        primaryArtifactsReduced.add(primaryArtifact);
                    }
                    if (launcherDef.isTransitive()) {
                        try {
                            logger.log(CAT.resolving(primaryArtifacts.size()));
                            final ArtifactResolutionResult rv = resolver.resolveTransitively(primaryArtifactsReduced, project.getArtifact(), remoteRepositories, localRepository, artifactMetadataSource);
                            @SuppressWarnings("unchecked") final Set<Artifact> result = rv.getArtifacts();
                            resultArtifacts.addAll(result);
                            resultArtifacts.addAll(primaryArtifactsReduced);
                            logger.log(CAT.resolved(primaryArtifacts.size(), result.size()));
                        } catch (ArtifactResolutionException e) {
                            throw new MojoExecutionException(launcherDef.getId() + ": transitive resolving failed", e);
                        } catch (ArtifactNotFoundException e) {
                            throw new MojoExecutionException(launcherDef.getId() + ": artifact not found", e);
                        }
                    }
                    final LauncherBean launcherBean = installerBean.addNewLauncher();
                    launcherBean.setId(launcherDef.getId());
                    String mainclass = launcherDef.getMainClass();
                    if (mainclass == null) {
                        final String f = resultArtifacts.iterator().next().getFile().getCanonicalPath();
                        final String extraSlash = f.startsWith("/") ? "" : "/";
                        final URL url = new URL("jar:file://" + extraSlash + f + "!/META-INF/MANIFEST.MF");
                        final InputStream is = url.openStream();
                        try {
                            final Manifest manifest = new Manifest(is);
                            final Object mco = manifest.getMainAttributes().get(Attributes.Name.MAIN_CLASS);
                            if (mco == null) {
                                throw new MojoFailureException(String.format("No mainclass specified for %s and none found in manifest of %s", launcherDef.getId(), url));
                            }
                            mainclass = mco.toString();
                            logger.log(CAT.mainclassAuto(launcherDef.getId(), mainclass, url));
                        } finally {
                            is.close();
                        }
                    }
                    launcherBean.setMainclass(mainclass);
                    if (usingSelf) {
                        final PathItemBean el = launcherBean.addNewPathitem();
                        final Artifact ra = project.getArtifact();
                        el.setGroupId(ra.getGroupId());
                        el.setArtifactId(ra.getArtifactId());
                        el.setVersion(ra.getVersion());
                        el.setType(ra.getType());
                    }
                    for (Artifact ra : resultArtifacts) {
                        final PathItemBean el = launcherBean.addNewPathitem();
                        el.setGroupId(ra.getGroupId());
                        el.setArtifactId(ra.getArtifactId());
                        if (Artifact.SCOPE_SYSTEM.equals(ra.getScope())) {
                            el.setVersion("SYSTEM");
                        } else {
                            el.setVersion(ra.getVersion());
                        }
                        final String classifier = ra.getClassifier();
                        if (classifier != null) {
                            el.setClassifier(classifier);
                        }
                        el.setType(ra.getType());
                    }
                }
                final XmlOptions options = new XmlOptions();
                options.setCharacterEncoding("UTF-8");
                options.setSavePrettyPrintIndent(2);
                options.setUseDefaultNamespace();
                options.setSavePrettyPrint();
                doc.save(outputFile, options);
                logger.log(CAT.output(outputFile));
            } catch (IOException e) {
                throw new MojoFailureException("writing to " + outputFile, e);
            }
        }
        final String installerVersion = BbxClassLoaderUtils.lookupManifestByGA("net.sf.buildbox.maven", "installer").getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
        logger.log(CAT.shell(installerVersion, project.getGroupId(), project.getArtifactId(), project.getVersion()));
    }
}
