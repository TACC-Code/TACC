class BackupThread extends Thread {
    private void copyArtifact(File location, File plugins_dir) throws MojoExecutionException {
        try {
            FileUtils.copyFileToDirectory(location, plugins_dir);
        } catch (IOException e) {
            throw new MojoExecutionException("Error copying file ", e);
        }
    }
}
