class BackupThread extends Thread {
    public byte[] getFirstDigest() {
        if (null != _firstSegment) {
            return _firstSegment.digest();
        } else {
            return null;
        }
    }
}
