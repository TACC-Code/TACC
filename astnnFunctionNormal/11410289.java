class BackupThread extends Thread {
    public static byte[] computeMessageDigestOf(File file) throws IOException, FileNotFoundException {
        MessageDigest md = getMessageDigest();
        FileInputStream is = new FileInputStream(file);
        DigestInputStream mdis = new DigestInputStream(is, md);
        byte buf[] = new byte[2048];
        while (mdis.read(buf) > 0) ;
        byte[] result = md.digest();
        is.close();
        return result;
    }
}
