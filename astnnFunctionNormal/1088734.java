class BackupThread extends Thread {
    public static void main(String[] args) throws IOException {
        FileInputStream fis = new FileInputStream(LockExam1.PATH);
        FileChannel ch = fis.getChannel();
        System.err.println("Trying to lock from FileInputStream");
        FileLock lock = ch.lock(0, Long.MAX_VALUE, true);
        System.err.println("Success on lock : " + lock);
        fis.close();
    }
}
