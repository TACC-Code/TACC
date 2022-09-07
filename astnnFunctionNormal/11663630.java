class BackupThread extends Thread {
    protected void signSamlAssertion(final SAMLAssertion samlAssertion) throws Exception {
        final Crypto issuerCrypto;
        final String issuerKeyPassword;
        final String issuerKeyName;
        Properties properties = new Properties();
        URL url = Loader.getResource(this.issuerKeyProperties);
        properties.load(url.openStream());
        String cryptoProp = properties.getProperty("org.apache.ws.security.saml.issuer.cryptoProp.file");
        issuerCrypto = CryptoFactory.getInstance(cryptoProp);
        issuerKeyName = properties.getProperty("org.apache.ws.security.saml.issuer.key.name");
        issuerKeyPassword = properties.getProperty("org.apache.ws.security.saml.issuer.key.password");
        X509Certificate[] issuerCerts = issuerCrypto.getCertificates(issuerKeyName);
        java.security.Key issuerPK = issuerCrypto.getPrivateKey(issuerKeyName, issuerKeyPassword);
        String sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_RSA;
        String pubKeyAlgo = issuerCerts[0].getPublicKey().getAlgorithm();
        log.debug("automatic sig algo detection: " + pubKeyAlgo);
        if (pubKeyAlgo.equalsIgnoreCase("DSA")) {
            sigAlgo = XMLSignature.ALGO_ID_SIGNATURE_DSA;
        }
        samlAssertion.sign(sigAlgo, issuerPK, Arrays.asList(issuerCerts));
    }
}
