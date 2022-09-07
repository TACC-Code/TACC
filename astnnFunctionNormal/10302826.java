class BackupThread extends Thread {
    public static boolean compare(String file1, String file2) throws IOException {
        FileChannel fc1 = null;
        FileChannel fc2 = null;
        try {
            FileInputStream fis1 = new FileInputStream(new File(file1));
            fc1 = fis1.getChannel();
            int sz = (int) fc1.size();
            MappedByteBuffer bb1 = fc1.map(FileChannel.MapMode.READ_ONLY, 0, sz);
            FileInputStream fis2 = new FileInputStream(new File(file2));
            fc2 = fis2.getChannel();
            int sz2 = (int) fc2.size();
            MappedByteBuffer bb2 = fc2.map(FileChannel.MapMode.READ_ONLY, 0, sz2);
            while (bb1.hasRemaining() && bb2.hasRemaining()) {
                if (bb1.get() != bb2.get()) return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                fc1.close();
            } catch (Exception e) {
            }
            try {
                fc2.close();
            } catch (Exception e) {
            }
        }
        return true;
    }
}
