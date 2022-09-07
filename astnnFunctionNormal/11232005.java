class BackupThread extends Thread {
    public void doDownload(HttpRequest request, long contentLength, OutputStream clientOut) {
        SpeedThread speedThread = new SpeedThread(this);
        buffer = new CircularDownloadBuffer(bufferSize);
        try {
            downloadThreads = new DownloadThread[downloadThreadCount];
            for (int i = 0; i < downloadThreadCount; i++) {
                downloadThreads[i] = new DownloadThread(request, buffer, this);
                downloadThreads[i].start();
            }
            speedThread.start();
            Thread.sleep(2000);
            byte readBuffer[] = new byte[4096];
            int readLength = 4096;
            while (bytesSent < contentLength - 1 && !downloadFinished) {
                if (readLength + bytesSent >= contentLength) readLength = (int) (contentLength - bytesSent - 1);
                int newBytes = buffer.read(readBuffer, readLength);
                clientOut.write(readBuffer, 0, newBytes);
                bytesSent += newBytes;
            }
            clientOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            downloadFinished = true;
            buffer.quit();
        }
        log.debug("Manager - finished sent: " + bytesSent);
    }
}
