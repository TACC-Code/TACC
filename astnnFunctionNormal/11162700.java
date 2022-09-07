class BackupThread extends Thread {
    @Override
    public long digest() {
        byte[] d = md5.digest();
        long hash = d[3] & 0xFF;
        hash += (d[2] & 0xFF) << 8;
        hash += (d[1] & 0xFF) << 16;
        hash += ((long) (d[0] & 0xFF)) << 24;
        return hash;
    }
}
