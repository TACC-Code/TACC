class BackupThread extends Thread {
    public String createPasswordHash(String password) {
        if (password == null || (_hashAlgorithm == null && _hashEncoding == null)) {
            return password;
        }
        if (logger.isDebugEnabled()) logger.debug("Creating password hash for [" + password + "] with algorithm/encoding [" + _hashAlgorithm + "/" + _hashEncoding + "]");
        if ("CRYPT".equalsIgnoreCase(_hashAlgorithm)) {
            String salt = password.substring(0, _saltLength);
            return Crypt.crypt(salt, password);
        }
        byte[] passBytes;
        String passwordHash = null;
        try {
            if (_hashCharset == null) passBytes = password.getBytes(); else passBytes = password.getBytes(_hashCharset);
        } catch (UnsupportedEncodingException e) {
            logger.error("charset " + _hashCharset + " not found. Using platform default.");
            passBytes = password.getBytes();
        }
        try {
            byte[] hash;
            if (_hashAlgorithm != null) hash = getDigest().digest(passBytes); else hash = passBytes;
            if ("BASE64".equalsIgnoreCase(_hashEncoding)) {
                passwordHash = CipherUtil.encodeBase64(hash);
            } else if ("HEX".equalsIgnoreCase(_hashEncoding)) {
                passwordHash = CipherUtil.encodeBase16(hash);
            } else if (_hashEncoding == null) {
                logger.error("You must specify a hashEncoding when using hashAlgorithm");
            } else {
                logger.error("Unsupported hash encoding format " + _hashEncoding);
            }
        } catch (Exception e) {
            logger.error("Password hash calculation failed : \n" + e.getMessage() != null ? e.getMessage() : e.toString(), e);
        }
        return passwordHash;
    }
}
