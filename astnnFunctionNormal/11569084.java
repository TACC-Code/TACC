class BackupThread extends Thread {
    private byte[] generatePeerID(byte[] unhashedID) {
        byte[] peerID = null;
        MessageDigest digest = null;
        try {
            P2POptions options = sharedManager.getOptions();
            switch(options.getHashAlgorithm()) {
                case P2PPUtils.SHA1_HASH_ALGORITHM:
                    digest = MessageDigest.getInstance("SHA-1");
                    peerID = digest.digest(unhashedID);
                    break;
                case P2PPUtils.SHA1_256_HASH_ALGORITHM:
                    digest = MessageDigest.getInstance("SHA256");
                    peerID = digest.digest(unhashedID);
                    break;
                case P2PPUtils.SHA1_512_HASH_ALGORITHM:
                    digest = MessageDigest.getInstance("SHA512");
                    peerID = digest.digest(unhashedID);
                    break;
                case P2PPUtils.MD4_HASH_ALGORITHM:
                    digest = MessageDigest.getInstance("MD4");
                    peerID = digest.digest(unhashedID);
                    break;
                case P2PPUtils.MD5_HASH_ALGORITHM:
                    digest = MessageDigest.getInstance("MD5");
                    peerID = digest.digest(unhashedID);
                    break;
                case P2PPUtils.NONE_HASH_ALGORITHM:
                    break;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return peerID;
    }
}
