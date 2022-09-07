class BackupThread extends Thread {
    @Override
    public Phd get(String id) throws DataStoreException {
        checkIfNotYetInitialized();
        FileChannel fastaFileChannel = null;
        DefaultPhdFileDataStore dataStore = null;
        InputStream in = null;
        try {
            Range range = recordLocations.getRangeFor(id);
            MappedByteBuffer buf = new FileInputStream(phdBall).getChannel().map(FileChannel.MapMode.READ_ONLY, range.getStart(), range.size());
            in = new ByteBufferInputStream(buf);
            dataStore = new DefaultPhdFileDataStore();
            PhdParser.parsePhd(in, dataStore);
            return dataStore.get(id);
        } catch (IOException e) {
            throw new DataStoreException("error getting " + id, e);
        } finally {
            IOUtil.closeAndIgnoreErrors(fastaFileChannel);
            IOUtil.closeAndIgnoreErrors(dataStore);
            IOUtil.closeAndIgnoreErrors(in);
        }
    }
}
