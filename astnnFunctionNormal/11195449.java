class BackupThread extends Thread {
    protected byte[] createHashFromString(byte[] buffer, long length) {
        MD4 md4 = new MD4();
        md4.update(buffer, 0, (int) length);
        return md4.digest();
    }
}
