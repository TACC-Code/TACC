class BackupThread extends Thread {
    public static CountAndDigest readRandomFile(InputStream in) throws IOException {
        try {
            DigestInputStream dis = new DigestInputStream(in, MessageDigest.getInstance(CCNDigestHelper.DEFAULT_DIGEST_ALGORITHM));
            byte[] buf = new byte[BUF_SIZE];
            int count = 0;
            int read = 0;
            while (read >= 0) {
                read = dis.read(buf);
                if (read > 0) count += read;
            }
            return new CountAndDigest(count, dis.getMessageDigest().digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find digest algorithm: " + CCNDigestHelper.DEFAULT_DIGEST_ALGORITHM);
        }
    }
}
