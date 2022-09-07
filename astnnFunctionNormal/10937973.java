class BackupThread extends Thread {
    public synchronized boolean readIndex() throws IOException, FeedFormatException {
        File f = getIndexFile();
        if (f == null || (!f.exists()) || (!f.canRead())) {
            return false;
        }
        setMode(Mode.READING_INDEX);
        FileInputStream in = new FileInputStream(f);
        FileChannel fc = in.getChannel();
        ObjectInputStream objIn = new ObjectInputStream(in);
        int count = 0;
        while (readIndexEntry(objIn)) {
            if ((count--) < 0) {
                updateProgress(((double) fc.position()) / fc.size());
                count = 10;
            }
        }
        end();
        setMode(Mode.READY);
        return true;
    }
}
