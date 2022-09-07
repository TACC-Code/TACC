class BackupThread extends Thread {
    protected String createPasswordHash(String password) throws SSOAuthenticationException {
        if (getHashAlgorithm() == null && getHashEncoding() == null) {
            return password;
        }
        if (logger.isDebugEnabled()) logger.debug("Creating password hash for [" + password + "] with algorithm/encoding [" + getHashAlgorithm() + "/" + getHashEncoding() + "]");
        if ("CRYPT".equalsIgnoreCase(getHashAlgorithm())) {
            String knownPassword = getPassword(getKnownCredentials());
            String salt = knownPassword != null && knownPassword.length() > 1 ? knownPassword.substring(0, _saltLenght) : "";
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
