class BackupThread extends Thread {
    public static byte[] createHash(final byte[]... dataToHash) throws Exception {
        final MessageDigest tmpDigester = createNewDigester();
        return tmpDigester.digest(Util.concatByteArrays(dataToHash));
    }
}
