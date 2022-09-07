class BackupThread extends Thread {
    public static String getDigest(String s) throws ApplicationException {
        String s1 = null;
        try {
            s1 = enc.encode(dig.digest(s.getBytes()));
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        return s1;
    }
}
