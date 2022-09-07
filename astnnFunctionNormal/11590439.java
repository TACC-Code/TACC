class BackupThread extends Thread {
    @Override
    public long write(FileChannel fileChannelIn) throws ArUnvalidIndexException, ArFileException {
        if (!this.isReady) {
            throw new ArUnvalidIndexException("Doc is not ready");
        }
        if (fileChannelIn == null) {
            throw new ArFileException("Arg FileChannelIn is not ready");
        }
        checkDirectory();
        if (!realFile.canWrite()) {
            throw new ArFileException("Doc is not writable");
        }
        FileChannel fileChannelOut = this.storage.getLegacy().getFileChannelOut(realFile, 0);
        long size = 0;
        long transfert = 0;
        try {
            size = fileChannelIn.size();
            transfert = fileChannelOut.transferFrom(fileChannelIn, 0, size);
        } catch (IOException e) {
            logger.info("Error during write");
            try {
                fileChannelOut.close();
            } catch (IOException e1) {
            }
            fileChannelOut = null;
            abortFile();
            throw new ArFileException("Doc cannot be writen");
        }
        try {
            fileChannelOut.close();
            fileChannelOut = null;
            fileChannelIn.close();
        } catch (IOException e) {
        }
        boolean retour = (size == transfert);
        if (retour) {
            this.updateLastTime();
        } else {
            size = -1;
            abortFile();
            throw new ArFileException("Cannot write doc");
        }
        return size;
    }
}
