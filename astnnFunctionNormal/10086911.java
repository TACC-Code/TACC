class BackupThread extends Thread {
    private FilePageServer(String aVolumeFileName, boolean shouldForceOpen) throws PageServerException {
        mVolumeFileName = aVolumeFileName;
        File file = new File(aVolumeFileName);
        if (!file.exists()) {
            throw new PageServerNotFoundException("Cannot find volume: " + aVolumeFileName);
        }
        if (!file.canRead()) {
            throw new PageServerException("Cannot read volume: " + aVolumeFileName + " because of permission settings");
        }
        mReadOnly = !file.canWrite();
        String openMode = (mReadOnly ? "r" : "rw");
        boolean success = false;
        try {
            mVolumeFile = new RandomAccessFile(file, openMode);
            FileLock fileLock = mVolumeFile.getChannel().tryLock();
            if (fileLock == null) {
                throw new PageServerException("Cannot lock volume: " + aVolumeFileName);
            }
            mHeader = new VolumeHeader();
            mHeader.read(mVolumeFile);
            if (mHeader.isOpen() && !shouldForceOpen) {
                throw new VolumeNeedsRecoveryException("Volume was not properly closed: " + aVolumeFileName);
            }
            mHeader.setOpenFlag(true);
            mHeader.write(mVolumeFile);
            mVolumeFile.getChannel().force(true);
            mPageSize = mHeader.getPageSize();
            success = true;
        } catch (FileNotFoundException e) {
            throw new PageServerNotFoundException("Cannot find volume: " + aVolumeFileName, e);
        } catch (PageServerException e) {
            throw e;
        } catch (Throwable t) {
            throw new PageServerException("Cannot open volume " + aVolumeFileName + ": " + t, t);
        } finally {
            if (!success) {
                if (mVolumeFile != null) {
                    try {
                        mVolumeFile.close();
                    } catch (Exception e) {
                    }
                    mVolumeFile = null;
                }
            }
        }
    }
}
