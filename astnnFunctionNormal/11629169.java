class BackupThread extends Thread {
    private void copyFile(File source, File target) throws MavenFilteringException, IOException, MojoExecutionException {
        if (filtering && !isNonFilteredExtension(source.getName())) {
            mavenFileFilter.copyFile(source, target, true, getFilterWrappers(), null);
        } else {
            FileUtils.copyFile(source, target);
        }
    }
}
