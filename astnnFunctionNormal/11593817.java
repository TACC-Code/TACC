class BackupThread extends Thread {
    private String getHash() {
        String pincode = jEdit.getProperty("remotecontrol.pincode", "1234");
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] digest1 = digest.digest((pincode + challenge).getBytes(RemoteServer.CHARSET));
            return toHexString(digest1);
        } catch (NoSuchAlgorithmException e) {
            Log.log(Log.ERROR, this, e, e);
        }
        return "";
    }
}
