class BackupThread extends Thread {
    public byte[] calculateChecksum(byte[] baseKey, int usage, byte[] input, int start, int len) throws GeneralSecurityException {
        if (debug) {
            System.out.println("ARCFOUR: calculateChecksum with usage = " + usage);
        }
        if (!KeyUsage.isValid(usage)) {
            throw new GeneralSecurityException("Invalid key usage number: " + usage);
        }
        byte[] Ksign = null;
        try {
            byte[] ss = "signaturekey".getBytes();
            byte[] new_ss = new byte[ss.length + 1];
            System.arraycopy(ss, 0, new_ss, 0, ss.length);
            Ksign = getHmac(baseKey, new_ss);
        } catch (Exception e) {
            GeneralSecurityException gse = new GeneralSecurityException("Calculate Checkum Failed!");
            gse.initCause(e);
            throw gse;
        }
        byte[] salt = getSalt(usage);
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            GeneralSecurityException gse = new GeneralSecurityException("Calculate Checkum Failed!");
            gse.initCause(e);
            throw gse;
        }
        messageDigest.update(salt);
        messageDigest.update(input, start, len);
        byte[] md5tmp = messageDigest.digest();
        byte[] hmac = getHmac(Ksign, md5tmp);
        if (debug) {
            traceOutput("hmac", hmac, 0, hmac.length);
        }
        if (hmac.length == getChecksumLength()) {
            return hmac;
        } else if (hmac.length > getChecksumLength()) {
            byte[] buf = new byte[getChecksumLength()];
            System.arraycopy(hmac, 0, buf, 0, buf.length);
            return buf;
        } else {
            throw new GeneralSecurityException("checksum size too short: " + hmac.length + "; expecting : " + getChecksumLength());
        }
    }
}
