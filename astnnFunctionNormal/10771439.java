class BackupThread extends Thread {
    protected void deploy(Collection<File> filesToDeploy, File sourceDir, File appserverHomeDir) throws Exception {
        String relativeDeployDirPath = getDescriptor().getAttribute(ATTR_STAGE_DEPLOY_DIR).getValue();
        File deployDir = new File(appserverHomeDir, relativeDeployDirPath);
        for (File stageFile : filesToDeploy) {
            String relativeStageFilename = stageFile.getPath().substring(1 + sourceDir.getPath().length());
            File deployedFile = new File(deployDir, relativeStageFilename);
            if (deployedFile.exists()) {
                String stageFileDigest = TolvenMessageDigest.checksum(stageFile.toURI().toURL(), MESSAGE_DIGEST_ALGORITHM);
                String deployedFileDigest = TolvenMessageDigest.checksum(deployedFile.toURI().toURL(), MESSAGE_DIGEST_ALGORITHM);
                if (deployedFileDigest.equals(stageFileDigest)) {
                    continue;
                }
            }
            logger.debug("Deploy " + stageFile.getPath() + " to " + deployedFile.getPath());
            FileUtils.copyFile(stageFile, deployedFile);
        }
    }
}
