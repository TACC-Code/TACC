class BackupThread extends Thread {
    public DeployPackagesResponse deployPackages(Set<ResourcePackageDetails> packages, ContentServices contentServices) {
        if (packages.size() != 1) {
            log.warn("Request to update an EAR/WAR file contained multiple packages: " + packages);
            DeployPackagesResponse response = new DeployPackagesResponse(ContentResponseResult.FAILURE);
            response.setOverallRequestErrorMessage("When updating an EAR/WAR, only one EAR/WAR can be updated at a time.");
            return response;
        }
        ResourcePackageDetails packageDetails = packages.iterator().next();
        log.debug("Updating EAR/WAR file '" + this.deploymentFile + "' using [" + packageDetails + "]...");
        if (!this.deploymentFile.exists()) {
            return failApplicationDeployment("Could not find application to update at location: " + this.deploymentFile, packageDetails);
        }
        log.debug("Writing new EAR/WAR bits to temporary file...");
        File tempFile;
        try {
            tempFile = writeNewAppBitsToTempFile(contentServices, packageDetails);
        } catch (Exception e) {
            return failApplicationDeployment("Error writing new application bits to temporary file - cause: " + e, packageDetails);
        }
        log.debug("Wrote new EAR/WAR bits to temporary file '" + tempFile + "'.");
        boolean deployExploded = this.deploymentFile.isDirectory();
        File backupOfOriginalFile = new File(this.deploymentFile.getPath() + BACKUP_FILE_EXTENSION);
        log.debug("Backing up existing EAR/WAR '" + this.deploymentFile + "' to '" + backupOfOriginalFile + "'...");
        try {
            if (backupOfOriginalFile.exists()) FileUtils.forceDelete(backupOfOriginalFile);
            if (this.deploymentFile.isDirectory()) FileUtils.copyDirectory(this.deploymentFile, backupOfOriginalFile, true); else FileUtils.copyFile(this.deploymentFile, backupOfOriginalFile, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to backup existing EAR/WAR '" + this.deploymentFile + "' to '" + backupOfOriginalFile + "'.");
        }
        try {
            DeploymentManager deploymentManager = ProfileServiceFactory.getDeploymentManager();
            DeploymentProgress progress = deploymentManager.stop(this.deploymentName);
            DeploymentUtils.run(progress);
        } catch (Exception e) {
            throw new RuntimeException("Failed to stop deployment [" + this.deploymentName + "].", e);
        }
        try {
            DeploymentManager deploymentManager = ProfileServiceFactory.getDeploymentManager();
            DeploymentProgress progress = deploymentManager.remove(this.deploymentName);
            DeploymentUtils.run(progress);
        } catch (Exception e) {
            throw new RuntimeException("Failed to remove deployment [" + this.deploymentName + "].", e);
        }
        log.debug("Deploying '" + tempFile + "'...");
        File deployDir = this.deploymentFile.getParentFile();
        try {
            DeploymentUtils.deployArchive(tempFile, deployDir, deployExploded);
        } catch (Exception e) {
            log.debug("Redeploy failed - rolling back to original archive...", e);
            String errorMessage = ThrowableUtil.getAllMessages(e);
            try {
                FileUtils.forceDelete(this.deploymentFile);
                DeploymentUtils.deployArchive(backupOfOriginalFile, deployDir, deployExploded);
                errorMessage += " ***** ROLLED BACK TO ORIGINAL APPLICATION FILE. *****";
            } catch (Exception e1) {
                log.debug("Rollback failed!", e1);
                errorMessage += " ***** FAILED TO ROLLBACK TO ORIGINAL APPLICATION FILE. *****: " + ThrowableUtil.getAllMessages(e1);
            }
            log.info("Failed to update EAR/WAR file '" + this.deploymentFile + "' using [" + packageDetails + "].");
            return failApplicationDeployment(errorMessage, packageDetails);
        }
        deleteBackupOfOriginalFile(backupOfOriginalFile);
        persistApplicationVersion(packageDetails, this.deploymentFile);
        DeployPackagesResponse response = new DeployPackagesResponse(ContentResponseResult.SUCCESS);
        DeployIndividualPackageResponse packageResponse = new DeployIndividualPackageResponse(packageDetails.getKey(), ContentResponseResult.SUCCESS);
        response.addPackageResponse(packageResponse);
        log.debug("Updated EAR/WAR file '" + this.deploymentFile + "' successfully - returning response [" + response + "]...");
        return response;
    }
}
