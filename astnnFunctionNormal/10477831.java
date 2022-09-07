class BackupThread extends Thread {
    @Override
    public boolean canRead() {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            file.delete();
        }
        try {
            fc = new java.io.RandomAccessFile(file, "rw").getChannel();
            fl = fc.tryLock();
            if (fl == null) {
                fc.close();
                return false;
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
}
