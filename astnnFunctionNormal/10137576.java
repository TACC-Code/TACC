class BackupThread extends Thread {
    private String createDigest(final String uname, final String pwd) throws AuthenticationException {
        LOG.trace("enter DigestScheme.createDigest(String, String, Map)");
        final String digAlg = "MD5";
        String uri = getParameter("uri");
        String realm = getParameter("realm");
        String nonce = getParameter("nonce");
        String qop = getParameter("qop");
        String method = getParameter("methodname");
        String algorithm = getParameter("algorithm");
        if (algorithm == null) {
            algorithm = "MD5";
        }
        String charset = getParameter("charset");
        if (charset == null) {
            charset = "ISO-8859-1";
        }
        if (qopVariant == QOP_AUTH_INT) {
            LOG.warn("qop=auth-int is not supported");
            throw new AuthenticationException("Unsupported qop in HTTP Digest authentication");
        }
        MessageDigest md5Helper;
        try {
            md5Helper = MessageDigest.getInstance(digAlg);
        } catch (Exception e) {
            throw new AuthenticationException("Unsupported algorithm in HTTP Digest authentication: " + digAlg);
        }
        StringBuffer tmp = new StringBuffer(uname.length() + realm.length() + pwd.length() + 2);
        tmp.append(uname);
        tmp.append(':');
        tmp.append(realm);
        tmp.append(':');
        tmp.append(pwd);
        String a1 = tmp.toString();
        if (algorithm.equals("MD5-sess")) {
            String tmp2 = encode(md5Helper.digest(EncodingUtil.getBytes(a1, charset)));
            StringBuffer tmp3 = new StringBuffer(tmp2.length() + nonce.length() + cnonce.length() + 2);
            tmp3.append(tmp2);
            tmp3.append(':');
            tmp3.append(nonce);
            tmp3.append(':');
            tmp3.append(cnonce);
            a1 = tmp3.toString();
        } else if (!algorithm.equals("MD5")) {
            LOG.warn("Unhandled algorithm " + algorithm + " requested");
        }
        String md5a1 = encode(md5Helper.digest(EncodingUtil.getBytes(a1, charset)));
        String a2 = null;
        if (qopVariant == QOP_AUTH_INT) {
            LOG.error("Unhandled qop auth-int");
        } else {
            a2 = method + ":" + uri;
        }
        String md5a2 = encode(md5Helper.digest(EncodingUtil.getAsciiBytes(a2)));
        String serverDigestValue;
        if (qopVariant == QOP_MISSING) {
            LOG.debug("Using null qop method");
            StringBuffer tmp2 = new StringBuffer(md5a1.length() + nonce.length() + md5a2.length());
            tmp2.append(md5a1);
            tmp2.append(':');
            tmp2.append(nonce);
            tmp2.append(':');
            tmp2.append(md5a2);
            serverDigestValue = tmp2.toString();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Using qop method " + qop);
            }
            String qopOption = getQopVariantString();
            StringBuffer tmp2 = new StringBuffer(md5a1.length() + nonce.length() + NC.length() + cnonce.length() + qopOption.length() + md5a2.length() + 5);
            tmp2.append(md5a1);
            tmp2.append(':');
            tmp2.append(nonce);
            tmp2.append(':');
            tmp2.append(NC);
            tmp2.append(':');
            tmp2.append(cnonce);
            tmp2.append(':');
            tmp2.append(qopOption);
            tmp2.append(':');
            tmp2.append(md5a2);
            serverDigestValue = tmp2.toString();
        }
        String serverDigest = encode(md5Helper.digest(EncodingUtil.getAsciiBytes(serverDigestValue)));
        return serverDigest;
    }
}
