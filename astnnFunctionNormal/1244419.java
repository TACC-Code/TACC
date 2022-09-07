class BackupThread extends Thread {
    public void load(File pac) throws java.io.FileNotFoundException, java.io.IOException {
        path = pac;
        RandomAccessFile fileObject = null;
        try {
            fileObject = new RandomAccessFile(path, "r");
            channel = fileObject.getChannel();
            mmap = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileObject.length());
        } catch (NonReadableChannelException e) {
            System.err.println("BUG!! used unreadable channel to create mmap. Message: " + e);
            throw e;
        } catch (NonWritableChannelException e) {
            System.err.println("BUG!! Tried to create a writable mmap. Message: " + e);
            throw e;
        } catch (IllegalArgumentException e) {
            System.err.println("BUG!! Tried to create mmap with bad parameters. Message: " + e);
            throw e;
        } finally {
            if (fileObject != null) fileObject.close();
        }
    }
}
