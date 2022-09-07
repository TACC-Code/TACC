class BackupThread extends Thread {
    public DigestInfo preSign(List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain) throws NoSuchAlgorithmException {
        LOG.debug("preSign");
        HttpSession session = getHttpSession();
        String toBeSigned = (String) session.getAttribute("toBeSigned");
        String digestAlgo = (String) session.getAttribute("digestAlgo");
        LOG.debug("digest algo: " + digestAlgo);
        String javaDigestAlgo = digestAlgo;
        if (digestAlgo.endsWith("-PSS")) {
            LOG.debug("RSA/PSS detected");
            javaDigestAlgo = digestAlgo.substring(0, digestAlgo.indexOf("-PSS"));
            LOG.debug("java digest algo: " + javaDigestAlgo);
        }
        MessageDigest messageDigest = MessageDigest.getInstance(javaDigestAlgo, new BouncyCastleProvider());
        byte[] digestValue = messageDigest.digest(toBeSigned.getBytes());
        String description = "Test Text Document";
        return new DigestInfo(digestValue, digestAlgo, description);
    }
}
