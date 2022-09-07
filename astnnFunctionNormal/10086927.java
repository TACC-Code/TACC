class BackupThread extends Thread {
    public void syncAllPages() throws PageServerException {
        try {
            mHeader.write(mVolumeFile);
            mVolumeFile.getChannel().force(false);
        } catch (Throwable t) {
            throw new PageServerException("Error syncing pages: " + t, t);
        }
    }
}
