class BackupThread extends Thread {
    public synchronized void setDownloadCompleted() throws TorrentException {
        if (isDownloadCompleted()) {
            TorrentMetainfo torrentMetaInfo = torrentContext.getTorrentMetainfo();
            String fileName = torrentMetaInfo.getInfo().getName().getValue();
            try {
                File destinationFile = new File(storeFoler, fileName);
                if (!destinationFile.isFile()) {
                    destinationFile.createNewFile();
                }
                FileChannel wChannel = new FileOutputStream(destinationFile, true).getChannel();
                int writtenCount = 0;
                for (PieceInfo pieceInfo : piecesInformation) {
                    byte[] data = pieceInfo.getValue();
                    writtenCount = writtenCount + data.length;
                    wChannel.write(ByteBuffer.wrap(data));
                }
                wChannel.close();
                if (writtenCount != fileLength) {
                    throw new TorrentException("File is of length " + fileLength + " but only " + writtenCount + " is written ");
                }
                log.info(fileName + " saved successfully to " + storeFoler);
            } catch (IOException e) {
                throw new TorrentException("Can not save the downloaded the torrent file to " + storeFoler + File.pathSeparator + fileName + "\n Reason " + e);
            }
        } else {
            throw new TorrentException("Storage Manager is notified as completed, but some of the pieces are yet to complete");
        }
    }
}
