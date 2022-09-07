class BackupThread extends Thread {
    public static ByteString createHashFromByteString(ByteString... dataToHash) throws Exception {
        final MessageDigest tmpDigester = createNewDigester();
        tmpDigester.update(Util.concatByteStrings(dataToHash).asReadOnlyByteBuffer());
        return ByteString.copyFrom(tmpDigester.digest());
    }
}
