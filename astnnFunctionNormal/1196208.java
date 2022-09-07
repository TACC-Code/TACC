class BackupThread extends Thread {
    public static String examine(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        MappedByteBuffer map = fc.map(MapMode.READ_ONLY, 0, f.length());
        final String ifid = examine(map);
        Log.d(TAG, "examined " + f + ", found IFID " + ifid);
        return ifid;
    }
}
