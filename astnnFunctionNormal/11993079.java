class BackupThread extends Thread {
    private void update() {
        WardenSHA1 sha1 = new WardenSHA1();
        sha1.update(this.randomSource1);
        sha1.update(this.random_data);
        sha1.update(this.randomSource2);
        this.random_data = ByteFromIntArray.LITTLEENDIAN.getByteArray(sha1.digest());
    }
}
