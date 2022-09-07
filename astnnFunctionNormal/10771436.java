class BackupThread extends Thread {
    protected void deployConfig() throws Exception {
        String appserverHomeDirname = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
        File appserverHomeDir = new File(appserverHomeDirname);
        if (!appserverHomeDir.exists()) {
            throw new RuntimeException("appserver home does not exist at: " + appserverHomeDir.getPath());
        }
        File appserverStageDir = new File(getStageDir(), appserverHomeDir.getName());
        if (!appserverStageDir.exists()) {
            appserverStageDir.mkdirs();
        }
        Collection<File> stageFiles = FileUtils.listFiles(appserverStageDir, null, true);
        for (File stageFile : stageFiles) {
            String relativeStageFilename = stageFile.getPath().substring(appserverStageDir.getPath().length());
            File deployedFile = new File(appserverHomeDir, relativeStageFilename);
            if (deployedFile.exists()) {
                String stageFileDigest = TolvenMessageDigest.checksum(stageFile.toURI().toURL(), MESSAGE_DIGEST_ALGORITHM);
                String deployedFileDigest = TolvenMessageDigest.checksum(deployedFile.toURI().toURL(), MESSAGE_DIGEST_ALGORITHM);
                if (deployedFileDigest.equals(stageFileDigest)) {
                    continue;
                }
            }
            logger.info("Deploy " + stageFile.getPath() + " to " + deployedFile.getPath());
            FileUtils.copyFile(stageFile, deployedFile);
        }
    }
}
