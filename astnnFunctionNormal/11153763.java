class BackupThread extends Thread {
    public static PipeID StringToPipeID(final PeerGroupID paramPeerGroupID, final String paramString) {
        MessageDigest sha = null;
        StringBuffer theStringBuffer = new StringBuffer(paramString);
        byte[] theSeed = new byte[theStringBuffer.length()];
        for (int iterator = 0; (iterator < 1024) && (iterator < theStringBuffer.length()); iterator++) {
            theSeed[iterator] = (byte) (theStringBuffer.charAt(iterator));
        }
        PipeID result;
        try {
            sha = MessageDigest.getInstance("SHA-1");
            sha.update(theSeed);
            result = (PipeID) IDFactory.newPipeID(paramPeerGroupID, sha.digest());
            logger.warn("TOOLBOX:\t " + result.toString());
            return result;
        } catch (java.security.NoSuchAlgorithmException theException) {
            System.out.println("SHA-1 is not available");
            result = (PipeID) IDFactory.newPipeID(paramPeerGroupID, theSeed);
            logger.warn("TOOLBOX:\t " + result.toString());
            return result;
        }
    }
}
