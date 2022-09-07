class BackupThread extends Thread {
    static String createFingerprint(byte[] certificateBytes, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(certificateBytes);
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            int b = digest[i] & 0xff;
            String hex = Integer.toHexString(b);
            if (i != 0) {
                sb.append(":");
            }
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
