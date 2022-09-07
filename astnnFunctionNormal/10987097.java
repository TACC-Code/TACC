class BackupThread extends Thread {
    private boolean transferDown(final File lFile, final DFSFile dFile) {
        DFSInputStream dssis = null;
        FileOutputStream los = null;
        long totalWritten = 0;
        try {
            byte[] readArray = new byte[blockSize];
            int rdSize = 0;
            dssis = new DFSInputStream(dFile);
            los = new FileOutputStream(lFile);
            while (rdSize != -1 && !progressMonitor.isCanceled()) {
                rdSize = dssis.read(readArray);
                if (rdSize > 0) {
                    los.write(readArray, 0, rdSize);
                    totalWritten += rdSize;
                    progressCounter += rdSize;
                    progressMonitor.setProgress(progressCounter);
                }
            }
            explorer.closeStreams(dssis, los);
            if (progressMonitor.isCanceled()) {
                lFile.delete();
                return false;
            }
            return true;
        } catch (IOException e) {
            logger.debug(e);
            lFile.delete();
            progressCounter -= totalWritten;
            explorer.closeStreams(dssis, los);
            return false;
        }
    }
}
