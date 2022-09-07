class BackupThread extends Thread {
    public void computeAuthenticatorISMG() {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String toMd5 = status + sharedSecret;
            authenticatorISMG = md.digest(toMd5.getBytes());
            logger.info("authenticatorSource: " + ByteUtil.toHexForLog(clientAuthenticatorSource));
        } catch (NoSuchAlgorithmException e) {
            logger.error("I don't know how to compute MD5!");
            System.exit(1);
        }
    }
}
