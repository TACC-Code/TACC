class BackupThread extends Thread {
    public void copyAssetFile(String srcFileName, String targetFilePath) {
        AIOUtils.copyAssetFile(mCtx, srcFileName, storagePath(targetFilePath));
    }
}
