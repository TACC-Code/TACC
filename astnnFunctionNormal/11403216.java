class BackupThread extends Thread {
    public static byte[] writeRandomFile(int bytes, OutputStream out) throws IOException {
        try {
            DigestOutputStream dos = new DigestOutputStream(out, MessageDigest.getInstance(CCNDigestHelper.DEFAULT_DIGEST_ALGORITHM));
            byte[] buf = new byte[BUF_SIZE];
            int count = 0;
            int towrite = 0;
            while (count < bytes) {
                random.nextBytes(buf);
                towrite = ((bytes - count) > buf.length) ? buf.length : (bytes - count);
                dos.write(buf, 0, towrite);
                count += towrite;
            }
            dos.flush();
            dos.close();
            return dos.getMessageDigest().digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Cannot find digest algorithm: " + CCNDigestHelper.DEFAULT_DIGEST_ALGORITHM);
        }
    }
}
