class BackupThread extends Thread {
        protected FileChannel getChannel() throws IOException {
            if ((fchannel == null) || (!fchannel.isOpen())) {
                String cache_path = getCachePath();
                RandomAccessFile file = new RandomAccessFile(cache_path, "rw");
                fchannel = file.getChannel();
            }
            return fchannel;
        }
}
