class BackupThread extends Thread {
    private void copyMetaInfFile(final File pSource, final File pTarget, final boolean pExistsBeforeCopying, final String pDescription) throws MojoExecutionException, IOException {
        if (pSource != null && pTarget != null) {
            if (!pSource.exists()) {
                throw new MojoExecutionException("The configured " + pDescription + " could not be found at " + pSource);
            }
            if (!pExistsBeforeCopying && pTarget.exists()) {
                getLog().warn("The configured " + pDescription + " overwrites another file from the classpath.");
            }
            FileUtils.copyFile(pSource, pTarget);
        }
    }
}
