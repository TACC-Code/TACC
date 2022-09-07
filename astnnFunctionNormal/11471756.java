class BackupThread extends Thread {
    private void preloadArtifacts(InstallerDocument doc, File libPath) throws MojoExecutionException, IOException {
        final InstallerBean inst = doc.getInstaller();
        for (LauncherBean cp : inst.getLauncherArray()) {
            logger.log(CAT.preloading(cp.getId()));
            for (PathItemBean cpe : cp.getPathitemArray()) {
                if ("SYSTEM".equals(cpe.getVersion())) continue;
                final Artifact a = artifactFactory.createArtifactWithClassifier(cpe.getGroupId(), cpe.getArtifactId(), cpe.getVersion(), cpe.getType(), cpe.getClassifier());
                resolve(a, false);
                if (libPath != null) {
                    String classifierSuffix = a.getClassifier();
                    if (classifierSuffix != null && !classifierSuffix.equals("")) {
                        classifierSuffix = "-" + classifierSuffix;
                    }
                    final File libFile = new File(libPath, String.format("%s-%s-%s%s.%s", a.getGroupId(), a.getArtifactId(), a.getVersion(), classifierSuffix, a.getType()));
                    libFile.getParentFile().mkdirs();
                    logger.debug("Copying %s to %s", a.getFile(), libFile);
                    FileUtils.copyFile(a.getFile(), libFile);
                }
            }
        }
    }
}
