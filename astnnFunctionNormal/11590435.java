class BackupThread extends Thread {
    @Override
    public void get(FileChannel fileChannelOut) throws ArUnvalidIndexException, ArFileException {
        if (!isReady) {
            throw new ArUnvalidIndexException("Doc is not ready");
        }
        if (fileChannelOut == null) {
            throw new ArUnvalidIndexException("FileChannel Out is not ready");
        }
        FileChannel fileChannelIn = this.storage.getLegacy().getFileChannelIn(realFile, 0);
        if (fileChannelIn == null) {
            throw new ArFileException("Doc is not readable");
        }
        long size = 0;
        long transfert = 0;
        try {
            size = fileChannelIn.size();
            transfert = fileChannelOut.transferFrom(fileChannelIn, 0, size);
            fileChannelOut.force(true);
            fileChannelIn.close();
            fileChannelIn = null;
            fileChannelOut.close();
        } catch (IOException e) {
            logger.error("Error during get:", e);
            if (fileChannelIn != null) {
                try {
                    fileChannelIn.close();
                } catch (IOException e1) {
                }
            }
            throw new ArFileException("Doc is not readable");
        }
        if (transfert == size) {
            position += size;
        }
        if (transfert != size) {
            throw new ArFileException("Doc is not fully readable");
        }
    }
}
