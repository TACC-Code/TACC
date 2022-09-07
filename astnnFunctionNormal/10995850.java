class BackupThread extends Thread {
    private void copyTmpFiles(Map<File, File> fileMap) {
        for (File tmpFile : fileMap.keySet()) {
            File snapshotMetadataFile = null;
            try {
                snapshotMetadataFile = fileMap.get(tmpFile);
                snapshotMetadataFile.getParentFile().mkdirs();
                logger.info("Write Snapshot metadata to " + snapshotMetadataFile);
                FileUtils.copyFile(tmpFile, snapshotMetadataFile);
            } catch (Exception ex) {
                throw new RuntimeException("Could not write snapshot metadata to: " + snapshotMetadataFile.getPath(), ex);
            }
        }
    }
}
