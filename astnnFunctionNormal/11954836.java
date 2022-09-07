class BackupThread extends Thread {
    private String getDigest(final byte[] buffer) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(buffer);
            final byte[] hash = md5.digest();
            String w = this.hex(hash);
            w = w.toLowerCase();
            return w;
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
