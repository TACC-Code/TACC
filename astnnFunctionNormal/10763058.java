class BackupThread extends Thread {
    private static String buildId(String name, String counterName) {
        final MessageDigest messageDigest = getMessageDigestInstance();
        messageDigest.update(name.getBytes());
        final byte[] digest = messageDigest.digest();
        final StringBuilder sb = new StringBuilder(digest.length * 2);
        sb.append(counterName);
        int j;
        for (final byte element : digest) {
            j = element < 0 ? 256 + element : element;
            if (j < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(j));
        }
        return sb.toString();
    }
}
