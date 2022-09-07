class BackupThread extends Thread {
    public static byte[] hash(X509Certificate certificate) {
        if (certificate == null) {
            throw new NullPointerException("certificate cannot be null");
        }
        try {
            byte[] encodedCertificate = certificate.getEncoded();
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(encodedCertificate);
            return md.digest();
        } catch (CertificateEncodingException ex) {
            throw new InternalFailureException("Unable to encode a X509v3 certificate (!)", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new InternalFailureException("No SHA1 algorithm (!)", ex);
        }
    }
}
