class BackupThread extends Thread {
    private void downloadOnce() throws FileSystemException {
        if (!this.downloaded) {
            final String failedMessage = "Failed to download S3 Object %s. %s";
            final String objectPath = getName().getPath();
            try {
                S3Object obj = this.service.getObject(this.bucket.getName(), getS3Key());
                this.logger.info(String.format("Downloading S3 Object: %s", objectPath));
                InputStream is = obj.getDataInputStream();
                if (obj.getContentLength() > 0) {
                    ReadableByteChannel rbc = Channels.newChannel(is);
                    FileChannel cacheFc = getCacheFileChannel();
                    cacheFc.transferFrom(rbc, 0, obj.getContentLength());
                    cacheFc.close();
                    rbc.close();
                } else {
                    is.close();
                }
            } catch (ServiceException e) {
                throw new FileSystemException(String.format(failedMessage, objectPath, e.getMessage()), e);
            } catch (IOException e) {
                throw new FileSystemException(String.format(failedMessage, objectPath, e.getMessage()), e);
            }
            this.downloaded = true;
        }
    }
}
