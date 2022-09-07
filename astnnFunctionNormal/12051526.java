class BackupThread extends Thread {
        public void close() throws IOException {
            super.close();
            byte[] actualDigest = getMessageDigest().digest();
            if (!MessageDigest.isEqual(expectedDigest, actualDigest)) {
                throw new FailedDigestCheckException(MessageFormat.format(FAILED_DIGEST_CHECK, getMessageDigest().getAlgorithm(), entryName, toHexStr(expectedDigest), toHexStr(actualDigest)));
            }
        }
}
