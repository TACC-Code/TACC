class BackupThread extends Thread {
    private static byte[] hash(final String expression) {
        byte[] result;
        MessageDigest digest;
        if (expression == null) {
            throw new IllegalArgumentException("Invalid null expression");
        }
        try {
            digest = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace(System.err);
            RuntimeException failure = new IllegalStateException("Could not get SHA-1 message");
            failure.initCause(noSuchAlgorithmException);
            throw failure;
        }
        try {
            byte[] expressionBytes = expression.getBytes("UTF-8");
            result = digest.digest(expressionBytes);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace(System.err);
            RuntimeException failure = new IllegalStateException("Could not encode expression as UTF-8");
            failure.initCause(unsupportedEncodingException);
            throw failure;
        }
        return result;
    }
}
