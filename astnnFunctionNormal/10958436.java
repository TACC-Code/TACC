class BackupThread extends Thread {
    public DigestInfo preSign(List<DigestInfo> digestInfos, List<X509Certificate> signingCertificateChain) throws NoSuchAlgorithmException {
        LOG.debug("preSign");
        HttpServletRequest httpServletRequest;
        try {
            httpServletRequest = (HttpServletRequest) PolicyContext.getContext("javax.servlet.http.HttpServletRequest");
        } catch (PolicyContextException e) {
            throw new RuntimeException("JACC error: " + e.getMessage());
        }
        HttpSession session = httpServletRequest.getSession();
        String toBeSigned = (String) session.getAttribute("toBeSigned");
        String digestAlgo = (String) session.getAttribute("digestAlgo");
        LOG.debug("digest algo: " + digestAlgo);
        MessageDigest messageDigest = MessageDigest.getInstance(digestAlgo, new BouncyCastleProvider());
        byte[] digestValue = messageDigest.digest(toBeSigned.getBytes());
        String description = "Test Text Document";
        return new DigestInfo(digestValue, digestAlgo, description);
    }
}
