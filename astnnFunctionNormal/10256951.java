class BackupThread extends Thread {
    private boolean setDownloadStreams() {
        try {
            if (compression) {
                fileTransferInputStream = new GZIPInputStream(session.getServer().getConnection().getFileTransferDataInputStream());
            } else {
                fileTransferInputStream = session.getServer().getConnection().getFileTransferDataInputStream();
            }
            if (resume) {
                fileTransferRandomAccessFile.seek(localFileSize);
            }
            fileTransferFileOutputStream = Channels.newOutputStream(fileTransferRandomAccessFile.getChannel());
            return true;
        } catch (Exception e) {
            try {
                fileTransferFileOutputStream.close();
            } catch (Exception e1) {
            }
            return false;
        }
    }
}
