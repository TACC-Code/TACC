class BackupThread extends Thread {
    public String getClientDigest() throws InfoCardProcessingException, CryptoException {
        BigInteger modulus = ValidatingBaseEnvelopedSignature.validate(getDoc());
        String sha1 = CryptoUtils.digest(modulus.toByteArray(), "SHA");
        return sha1;
    }
}
