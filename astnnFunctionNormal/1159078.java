class BackupThread extends Thread {
    public String getSharedSecret(String peerPubKey) {
        BigInteger primeInt = new BigInteger(1, decodeB64(PRIME));
        BigInteger peerPubInt = new BigInteger(1, decodeB64(peerPubKey));
        BigInteger shareInt = peerPubInt.modPow(_privateInt, primeInt);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(getBytes(shareInt));
            return encodeB64(hashed);
        } catch (NoSuchAlgorithmException e) {
            logger.debug("Algorithm for DH1080 shared secret hashing not available", e);
        }
        return null;
    }
}
