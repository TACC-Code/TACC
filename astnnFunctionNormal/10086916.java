class BackupThread extends Thread {
    public void disconnect() throws PageServerException {
        try {
            mHeader.setOpenFlag(false);
            mHeader.write(mVolumeFile);
            mVolumeFile.getChannel().force(true);
            mVolumeFile.close();
        } catch (Exception e) {
            throw new PageServerException("Could not properly close volume: " + e, e);
        } finally {
            mVolumeFile = null;
            mHeader = null;
        }
        sLogger.fine("FilePageServer disconnected/shutdown");
    }
}
