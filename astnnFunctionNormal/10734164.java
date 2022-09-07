class BackupThread extends Thread {
    public X509Certificate verify() throws GeneralSecurityException {
        byte[] b;
        if (twostep_) {
            b = digest_.digest();
            if (!Arrays.equals(b, md_)) {
                return null;
            }
            info_.update(sig_);
        }
        if (target_ instanceof SignedAndEnvelopedData) {
            SignedAndEnvelopedData saed;
            byte[] edig;
            saed = (SignedAndEnvelopedData) target_;
            edig = info_.getEncryptedDigest();
            b = saed.decryptBulkData(edig);
        } else {
            b = info_.getEncryptedDigest();
        }
        if (sig_.verify(b)) {
            return cert_;
        }
        return null;
    }
}
