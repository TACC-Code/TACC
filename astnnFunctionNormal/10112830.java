class BackupThread extends Thread {
    @Override
    public boolean visitReadHeader(SFFReadHeader readHeader) {
        try {
            SffWriter.writeReadHeader(readHeader, out);
        } catch (IOException e) {
            throw new IllegalStateException("error writing Sff read header ", e);
        }
        return true;
    }
}
