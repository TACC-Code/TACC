class BackupThread extends Thread {
    public static String hashFile(File file) {
        try {
            InputStream in = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("sha1");
            DigestInputStream din = new DigestInputStream(in, md);
            byte[] buffer = new byte[1048576];
            while (din.read(buffer) != -1) ;
            din.close();
            byte[] digest = md.digest();
            return String.format("%0" + (digest.length * 2) + "x", new BigInteger(1, digest));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
