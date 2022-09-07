class BackupThread extends Thread {
    private boolean writeLocalFileChecksum() {
        crc32.reset();
        readOffsets = 0;
        try {
            if (checksumReader == null) {
                checksumReader = new CheckedInputStream(Channels.newInputStream(fileTransferRandomAccessFile.getChannel()), crc32);
            }
            if (remoteFileSize < localFileSize) {
                maxOffsets = remoteFileSize;
            } else {
                maxOffsets = localFileSize;
            }
            while (!stopped && maxOffsets > readOffsets) {
                if (readOffsets + fileTransferBufferSize >= maxOffsets) {
                    bytesRead = checksumReader.read(fileTransferBuffer, 0, Long.valueOf(maxOffsets - readOffsets).intValue());
                } else {
                    bytesRead = checksumReader.read(fileTransferBuffer, 0, fileTransferBufferSize);
                }
                readOffsets += bytesRead;
            }
            localChecksum = checksumReader.getChecksum().getValue();
            session.getServer().getConnection().getFileTransferControlDataOutputStream().writeLong(localChecksum);
            session.getServer().getConnection().getFileTransferControlDataOutputStream().flush();
            fileTransferRandomAccessFile.seek(0);
            if (stopped) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
