class BackupThread extends Thread {
    public static byte[] digest(Object obj) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(DefaultSerializer.INSTANCE.serialize(obj));
        } catch (Exception e) {
            throw new SpaceError(e);
        }
    }
}
