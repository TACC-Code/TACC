class BackupThread extends Thread {
    public static void main(String args[]) throws Exception {
        boolean writer = false;
        String filename;
        if (args.length != 2) {
            System.out.println("Usage: [ -r | -w ] filename");
            return;
        }
        writer = args[0].equals("-w");
        filename = args[1];
        RandomAccessFile raf = new RandomAccessFile(filename, (writer) ? "rw" : "r");
        FileChannel fc = raf.getChannel();
        LockTest lockTest = new LockTest();
        if (writer) {
            lockTest.doUpdates(fc);
        } else {
            lockTest.doQueries(fc);
        }
    }
}
