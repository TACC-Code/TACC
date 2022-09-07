class BackupThread extends Thread {
    public static String md5Sum(byte[] input, int limit) {
        try {
            if (_md == null) {
                _md = MessageDigest.getInstance("MD5");
            }
            _md.reset();
            byte[] digest;
            if (limit == -1) {
                digest = _md.digest(input);
            } else {
                _md.update(input, 0, limit > input.length ? input.length : limit);
                digest = _md.digest();
            }
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                hexString.append(hexDigit(digest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
