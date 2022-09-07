class BackupThread extends Thread {
    private File storeBlobCopyToTempFile(InputStream in, File blobDir, long timeout) throws IOException {
        if (in == null) throw new NullPointerException("in");
        File tempFile = Files.createTempFile(null, ".temp", blobDir);
        FileChannel out = null;
        try {
            synchronized (this.lock) {
                if (this.removed) {
                    throw new IllegalStateException("Node has been removed.");
                } else {
                    Files.mkdirs(blobDir);
                    out = Files.createLockedOutputStream(tempFile, timeout, TimeUnit.MILLISECONDS).getChannel();
                }
            }
            FileChannels.transferFrom(in, out, -1);
            out.close();
            out = null;
            return tempFile;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Throwable ex) {
                }
                try {
                    tempFile.delete();
                } catch (Throwable ex) {
                }
                if (isRemoved()) throw new IllegalStateException("Node has been removed.");
            }
        }
    }
}
