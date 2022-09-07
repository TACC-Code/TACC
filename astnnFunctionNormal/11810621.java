class BackupThread extends Thread {
    public static String generateSignature(List<String> params, String secret) {
        Collections.sort(params);
        StringBuffer buffer = new StringBuffer();
        for (String param : params) {
            buffer.append(param);
        }
        buffer.append(secret);
        if (FacebookApi.log.isDebugEnabled()) FacebookApi.log.debug("signing: " + buffer);
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            StringBuffer result = new StringBuffer();
            for (byte b : md.digest(buffer.toString().getBytes())) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            if (FacebookApi.log.isDebugEnabled()) FacebookApi.log.debug("signature: " + result);
            return result.toString();
        } catch (java.security.NoSuchAlgorithmException ex) {
            FacebookApi.log.debug("MD5 does not appear to be supported" + ex);
            return "";
        }
    }
}
