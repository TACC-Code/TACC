class BackupThread extends Thread {
        public void flush(boolean close) throws IOException {
            if (!addrCache.isEmpty()) {
                purge();
            }
            raf.getChannel().force(true);
            if (close) {
                raf.close();
            }
        }
}
