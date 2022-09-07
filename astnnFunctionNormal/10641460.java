class BackupThread extends Thread {
    private static long generateClassId(byte[] someByteCodes) throws Exception {
        java.security.MessageDigest sha1Digest = java.security.MessageDigest.getInstance("SHA-1");
        byte[] sha1 = sha1Digest.digest(someByteCodes);
        long cid = (long) (sha1[0] & 0xff) | ((long) (sha1[1] & 0xff) << 8) | ((long) (sha1[2] & 0xff) << 16) | ((long) (sha1[3] & 0xff) << 24) | ((long) (sha1[4] & 0xff) << 32) | ((long) (sha1[5] & 0xff) << 40) | ((long) (sha1[6] & 0xff) << 48) | ((long) (sha1[7] & 0xff) << 56);
        if (cid >= ObjectSerializer.NULL_CID && cid <= ObjectSerializer.LAST_SYSTEM_CID) {
            cid += ObjectSerializer.LAST_SYSTEM_CID;
        }
        return cid;
    }
}
