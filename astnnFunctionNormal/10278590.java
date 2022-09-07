class BackupThread extends Thread {
    public Transferrer createTransferrer(TransferredFile file, long startedAt, long toDownload) {
        return new FileReceiver(controller, file.getChannel(), startedAt, toDownload);
    }
}
