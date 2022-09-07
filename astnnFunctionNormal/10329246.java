class BackupThread extends Thread {
    public byte[] calculateFileHash(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(bis, md5);
            md5.reset();
            while (dis.read() != -1 && !stop) ;
            if (!stop) return md5.digest();
        } catch (Exception e) {
        }
        return null;
    }
}
