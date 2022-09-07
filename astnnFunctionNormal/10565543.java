class BackupThread extends Thread {
    public static String md5Sum(File file) {
        InputStream istr = null;
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            istr = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int n;
            while ((n = istr.read(buffer)) != -1) {
                digest.update(buffer, 0, n);
            }
            istr.close();
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (Exception e) {
            Logger.getLogger(IOUtils.class).error(String.format("Could not get md5sum of %s: %s", file, e.getMessage()), e);
            return "";
        }
    }
}
