class BackupThread extends Thread {
    public static byte[] generateCertificateDigest(String digestAlg, X509Certificate cert) throws CertificateEncodingException, NoSuchAlgorithmException {
        byte[] id = null;
        try {
            byte[] encoding = cert.getEncoded();
            id = CCNDigestHelper.digest(digestAlg, encoding);
        } catch (CertificateEncodingException e) {
            Log.warning("Cannot encode certificate in PublisherID.generateCertificateID: " + e.getMessage());
            Log.warningStackTrace(e);
            throw e;
        }
        return id;
    }
}
