class BackupThread extends Thread {
    @Override
    public boolean visitReadData(SFFReadData readData) {
        try {
            SffWriter.writeReadData(readData, out);
        } catch (IOException e) {
            throw new IllegalStateException("error writing Sff read Data ", e);
        }
        return true;
    }
}
