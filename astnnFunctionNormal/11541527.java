class BackupThread extends Thread {
    public TorrentStorage(TorrentMetadata metadata, File tempTorrent) throws IOException {
        tempDir = new File(ConfigManager.getTempDir() + "/" + metadata.getName());
        this.torrentFile = tempTorrent;
        dataFile = new File(tempDir, DATA_FILENAME);
        stateFile = new File(tempDir, STATE_FILENAME);
        this.metadata = metadata;
        initFiles();
        dataChannel = new RandomAccessFile(dataFile, "rw").getChannel();
        stateChannel = new RandomAccessFile(stateFile, "rw").getChannel();
        initChunks();
    }
}
