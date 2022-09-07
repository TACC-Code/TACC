class BackupThread extends Thread {
    public String generateSignature(String requestString, String secretKey) {
        requestString = requestString + secretKey;
        StringBuilder result = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            for (byte b : md.digest(requestString.toString().getBytes())) {
                result.append(Integer.toHexString((b & 0xf0) >>> 4));
                result.append(Integer.toHexString(b & 0x0f));
            }
            return (result.toString());
        } catch (NoSuchAlgorithmException e) {
            return ("Error: no MD5 ");
        }
    }
}
