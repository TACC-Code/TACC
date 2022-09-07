class BackupThread extends Thread {
    private byte[] calculateInfoHash() {
        Map<String, Object> info = createInfoMap();
        byte[] infoBytes = BEncoder.bencode(info);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            return digest.digest(infoBytes);
        } catch (NoSuchAlgorithmException nsa) {
            throw new InternalError(nsa.toString());
        }
    }
}
